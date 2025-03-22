package com.ylab.finance_tracker.ui.servlet.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.StatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCostAnalysisByCategoryServletTest {
    private GetCostAnalysisByCategoryServlet servlet;
    @Mock
    private StatisticsService statisticsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        servlet = new GetCostAnalysisByCategoryServlet(statisticsService, objectMapper);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_Success() throws Exception {
        UserDTO user = new UserDTO(null, "test", "test@example.com", "password", null);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(user);
        when(request.getParameter("from")).thenReturn("2024-03-01");
        when(request.getParameter("to")).thenReturn("2024-03-31");

        Map<String, Object> mockReport = Map.of("category", "Food", "amount", 200);
        when(statisticsService.generateFinancialReport("test@example.com", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)))
                .thenReturn(mockReport.toString());

        servlet.doGet(request, response);
        writer.flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Food"));
        assertTrue(stringWriter.toString().contains("200"));
    }

    @Test
    void testDoGet_MissingParameters_BadRequest() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new UserDTO(null, "test@example.com", "test", "password", null));

        when(request.getParameter("from")).thenReturn(null);
        when(request.getParameter("to")).thenReturn(null);

        servlet.doGet(request, response);
        writer.flush();

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'from' or 'to' parameter");
    }

    @Test
    void testDoGet_SqlException_InternalServerError() throws Exception {
        UserDTO user = new UserDTO(null, "test@example.com", "test", "password", null);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(user);
        when(request.getParameter("from")).thenReturn("2024-03-01");
        when(request.getParameter("to")).thenReturn("2024-03-31");

        when(statisticsService.generateFinancialReport(anyString(), any(), any())).thenThrow(new SQLException("Database error"));

        servlet.doGet(request, response);
        writer.flush();

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
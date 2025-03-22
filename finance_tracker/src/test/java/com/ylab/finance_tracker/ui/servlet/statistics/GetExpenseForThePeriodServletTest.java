package com.ylab.finance_tracker.ui.servlet.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.common.TransactionType;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetExpenseForThePeriodServletTest {

    private GetExpenseForThePeriodServlet servlet;

    @Mock
    private StatisticsService statisticsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private StringWriter stringWriter;
    private UserDTO mockUser;

    @BeforeEach
    void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        servlet = new GetExpenseForThePeriodServlet(statisticsService, objectMapper);

        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        mockUser = new UserDTO(null, "test", "test@example.com", "password", null);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(mockUser);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_ReturnsExpenseForThePeriod() throws Exception {
        String from = "2024-03-01";
        String to = "2024-03-31";
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        when(statisticsService.getTotal(mockUser.getEmail(), fromDate, toDate, TransactionType.EXPENSE))
                .thenReturn(BigDecimal.valueOf(100.0));

        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);

        servlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("100.0"));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_InvalidDates_ReturnsBadRequest() throws Exception {
        when(request.getParameter("from")).thenReturn("invalid-date");
        when(request.getParameter("to")).thenReturn("invalid-date");

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoGet_SQLException_ReturnsInternalServerError() throws Exception {
        String from = "2024-03-01";
        String to = "2024-03-31";
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);

        when(statisticsService.getTotal(mockUser.getEmail(), fromDate, toDate, TransactionType.EXPENSE))
                .thenThrow(new SQLException("Database error"));

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
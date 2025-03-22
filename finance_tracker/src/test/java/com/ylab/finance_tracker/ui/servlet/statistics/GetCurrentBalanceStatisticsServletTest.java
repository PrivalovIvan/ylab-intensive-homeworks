package com.ylab.finance_tracker.ui.servlet.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCurrentBalanceStatisticsServletTest {
    private GetCurrentBalanceStatisticsServlet servlet;
    @Mock
    private StatisticsService statisticsService;
    private ObjectMapper objectMapper;
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
        objectMapper = new ObjectMapper();
        servlet = new GetCurrentBalanceStatisticsServlet(statisticsService, objectMapper);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    void testDoGet_Success() throws Exception {
        UserDTO mockUser = new UserDTO();
        mockUser.setEmail("test@example.com");

        when(session.getAttribute("currentUser")).thenReturn(mockUser);
        when(statisticsService.getCurrentBalance("test@example.com")).thenReturn(new BigDecimal("1000.50"));

        servlet.doGet(request, response);
        writer.flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(objectMapper.writeValueAsString(Map.of("balance", new BigDecimal("1000.50"))),
                stringWriter.toString().trim());
    }

    @Test
    void testDoGet_DatabaseError() throws Exception {
        UserDTO mockUser = new UserDTO();
        mockUser.setEmail("test@example.com");

        when(session.getAttribute("currentUser")).thenReturn(mockUser);
        when(statisticsService.getCurrentBalance("test@example.com")).thenThrow(new SQLException("DB Error"));

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
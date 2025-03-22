package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.BudgetDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.BudgetService;
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
import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServletTest {
    private BudgetServlet budgetServlet;
    @Mock
    private BudgetService budgetService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    private StringWriter stringWriter;
    private PrintWriter writer;
    @Mock
    private ValidatorDTO validatorDTO;

    @BeforeEach
    void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        budgetServlet = new BudgetServlet(budgetService, objectMapper, validatorDTO);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        UserDTO mockUser = new UserDTO(UUID.randomUUID(), "test", "test@example.com", "password", null);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(mockUser);
        lenient().when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_ReturnsBudget() throws Exception {
        YearMonth yearMonth = YearMonth.of(2024, 3);
        BudgetDTO mockBudget = new BudgetDTO(UUID.randomUUID(), "test@example.com", yearMonth, BigDecimal.valueOf(5000), BigDecimal.valueOf(1000));

        when(request.getParameter("yearMonth")).thenReturn("2024-03");
        when(budgetService.getBudget("test@example.com", yearMonth)).thenReturn(Optional.of(mockBudget));

        budgetServlet.doGet(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains("\"budget\":5000"));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_CreatesBudget() throws Exception {
        when(request.getParameter("yearMonth")).thenReturn("2024-04");
        when(request.getParameter("amountLimit")).thenReturn("3000");
        when(validatorDTO.isValid(any(BudgetDTO.class), eq(response))).thenReturn(true);

        budgetServlet.doPost(request, response);
        writer.flush();

        verify(budgetService).createBudget(any(BudgetDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPatch_AddsExpense() throws Exception {
        when(request.getParameter("yearMonth")).thenReturn("2024-05");
        when(request.getParameter("expense")).thenReturn("500");

        budgetServlet.doPut(request, response);
        writer.flush();

        verify(budgetService).addExpense(eq("test@example.com"), eq(YearMonth.of(2024, 5)), eq(new BigDecimal("500.0")));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }
}
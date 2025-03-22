package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.finance_tracker.common.TransactionType;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.TransactionDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.BudgetService;
import com.ylab.finance_tracker.usecase.service.TransactionService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServletTest {
    @Mock
    private TransactionService transactionService;
    @Mock
    private BudgetService budgetService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    private TransactionServlet transactionServlet;
    private StringWriter stringWriter;
    private PrintWriter writer;
    private UserDTO mockUser;
    @Mock
    private ValidatorDTO validatorDTO;

    @BeforeEach
    void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        transactionServlet = new TransactionServlet(transactionService, objectMapper, budgetService, validatorDTO);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        mockUser = new UserDTO(UUID.randomUUID(), "test", "test@example.com", "password", null);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(mockUser);
        lenient().when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_ReturnsTransactions() throws Exception {
        List<TransactionDTO> mockTransactions = List.of(
                new TransactionDTO(UUID.randomUUID(),
                        "test@example.com",
                        TransactionType.INCOME,
                        BigDecimal.valueOf(100),
                        "Salary", LocalDate.now(),
                        "Monthly salary"));

        when(transactionService.findAllTransactionUser("test@example.com")).thenReturn(mockTransactions);

        transactionServlet.doGet(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains("\"email\":\"test@example.com\""));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_CreatesTransaction() throws Exception {
        when(request.getParameter("type")).thenReturn("INCOME");
        when(request.getParameter("amount")).thenReturn("100.00");
        when(request.getParameter("category")).thenReturn("Salary");
        when(request.getParameter("description")).thenReturn("Monthly salary");
        when(validatorDTO.isValid(any(TransactionDTO.class), eq(response))).thenReturn(true);

        transactionServlet.doPost(request, response);
        writer.flush();

        verify(transactionService).createTransaction(any(TransactionDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPatch_UpdatesTransaction() throws Exception {
        UUID transactionId = UUID.randomUUID();
        when(request.getParameter("id")).thenReturn(transactionId.toString());
        when(request.getParameter("amount")).thenReturn("150.00");
        when(request.getParameter("category")).thenReturn("Updated Salary");
        when(request.getParameter("description")).thenReturn("Updated Monthly Salary");

        transactionServlet.doPut(request, response);
        writer.flush();

        verify(transactionService).updateAmount(mockUser.getEmail(), transactionId, BigDecimal.valueOf(150.00));
        verify(transactionService).updateCategory(mockUser.getEmail(), transactionId, "Updated Salary");
        verify(transactionService).updateDescription(mockUser.getEmail(), transactionId, "Updated Monthly Salary");
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete_DeletesTransaction() throws Exception {
        UUID transactionId = UUID.randomUUID();
        when(request.getParameter("id")).thenReturn(transactionId.toString());

        transactionServlet.doDelete(request, response);
        writer.flush();

        verify(transactionService).deleteTransaction(mockUser.getEmail(), transactionId);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.TransactionDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.AdministrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServletTest {
    private AdminServlet adminServlet;
    @Mock
    private AdministrationService adminService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private StringWriter stringWriter;
    private PrintWriter writer;
    @Mock
    private ValidatorDTO validatorDTO;

    @BeforeEach
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        adminServlet = new AdminServlet(adminService, objectMapper, validatorDTO);

        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_ReturnsAllUsers() throws Exception {
        when(request.getPathInfo()).thenReturn("/users");
        List<UserDTO> mockUsers = List.of(new UserDTO());
        when(adminService.findAllUsers()).thenReturn(mockUsers);

        adminServlet.doGet(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains("[") && stringWriter.toString().contains("]"));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_ReturnsAllTransactionsForUser() throws Exception {
        when(request.getPathInfo()).thenReturn("/users/transactions");
        when(request.getParameter("email")).thenReturn("user@example.com");
        List<TransactionDTO> mockTransactions = List.of(new TransactionDTO());
        when(adminService.findAllTransactionsOfUsers("user@example.com")).thenReturn(mockTransactions);

        adminServlet.doGet(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains("[") && stringWriter.toString().contains("]"));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_NoTransactionsFound() throws Exception {
        when(request.getPathInfo()).thenReturn("/users/transactions");
        when(request.getParameter("email")).thenReturn("user@example.com");
        when(adminService.findAllTransactionsOfUsers("user@example.com")).thenReturn(Collections.emptyList());

        adminServlet.doGet(request, response);
        writer.flush();

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete_UserDeleted() throws Exception {
        when(request.getPathInfo()).thenReturn("/users/delete");
        when(request.getParameter("email")).thenReturn("user@example.com");

        adminServlet.doDelete(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains("User deleted"));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(adminService).deleteUser("user@example.com");
    }

    @Test
    void testDoDelete_UserDeletionFails() throws Exception {
        when(request.getPathInfo()).thenReturn("/users/delete");
        when(request.getParameter("email")).thenReturn("user@example.com");
        doThrow(new SQLException("Database error")).when(adminService).deleteUser("user@example.com");

        adminServlet.doDelete(request, response);
        writer.flush();

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
    }
}

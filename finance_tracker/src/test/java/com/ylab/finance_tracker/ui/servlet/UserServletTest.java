package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.common.Role;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.UserService;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServletTest {
    private UserServlet userServlet;
    @Mock
    private UserService userService;
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
        userServlet = new UserServlet(userService, objectMapper, validatorDTO);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_ReturnsUserProfile() throws Exception {
        UserDTO mockUser = new UserDTO(UUID.randomUUID(), "test", "test@example.com", "12345", Role.USER);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(mockUser);
        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);

        userServlet.doGet(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains("\"email\":\"test@example.com\""));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_RegistrationSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/registration");
        when(request.getParameter("name")).thenReturn("do Post Registration");
        when(request.getParameter("email")).thenReturn("registration@example.com");
        when(request.getParameter("password")).thenReturn("password");
        when(validatorDTO.isValid(any(UserDTO.class), eq(response))).thenReturn(true);

        userServlet.doPost(request, response);
        writer.flush();

        verify(userService).register(any(UserDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        assertTrue(stringWriter.toString().contains("Registration successful"));
    }

    @Test
    void testDoPost_LoginSuccess() throws Exception {
        UserDTO mockUser = new UserDTO(UUID.randomUUID(), "do Post Login", "login@example.com", "password", Role.USER);
        when(request.getPathInfo()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("login@example.com");
        when(request.getParameter("password")).thenReturn("password");
        when(userService.login("login@example.com", "password")).thenReturn(mockUser);
        when(request.getSession(true)).thenReturn(session);

        userServlet.doPost(request, response);
        writer.flush();

        verify(session).setAttribute("currentUser", mockUser);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("Login successful"));
    }
}

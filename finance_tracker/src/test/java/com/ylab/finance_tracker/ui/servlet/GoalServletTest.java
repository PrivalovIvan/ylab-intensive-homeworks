package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.GoalDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.GoalService;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServletTest {
    private GoalServlet goalServlet;
    @Mock
    private GoalService goalService;
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
        goalServlet = new GoalServlet(goalService, objectMapper, validatorDTO);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        UserDTO mockUser = new UserDTO(UUID.randomUUID(), "test", "test@example.com", "password", null);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(mockUser);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_ReturnsGoals() throws Exception {
        List<GoalDTO> mockGoals = List.of(
                new GoalDTO(UUID.randomUUID(), "test@example.com", "New Car", BigDecimal.valueOf(10000), BigDecimal.valueOf(5000)));

        when(goalService.getUserGoals("test@example.com")).thenReturn(mockGoals);
        when(response.getWriter()).thenReturn(writer);

        goalServlet.doGet(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains("\"title\":\"New Car\""));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_CreatesGoal() throws Exception {
        when(request.getParameter("nameGoal")).thenReturn("House");
        when(request.getParameter("amount")).thenReturn("50000");
        when(validatorDTO.isValid(any(GoalDTO.class), eq(response))).thenReturn(true);

        goalServlet.doPost(request, response);
        writer.flush();

        verify(goalService).createGoal(any(GoalDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPatch_UpdatesGoal() throws Exception {
        String goalTitle = "New Car";
        when(request.getParameter("nameGoal")).thenReturn(goalTitle);
        when(request.getParameter("amount")).thenReturn("2000");

        GoalDTO existingGoal = new GoalDTO(UUID.randomUUID(), "test@example.com", goalTitle, BigDecimal.valueOf(10000), BigDecimal.valueOf(5000));
        when(goalService.getGoalByName("test@example.com", goalTitle)).thenReturn(existingGoal);
        when(validatorDTO.isValid(any(GoalDTO.class), eq(response))).thenReturn(true);

        goalServlet.doPut(request, response);
        writer.flush();

        verify(goalService).updateGoal(any(GoalDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDelete_DeletesGoal() throws Exception {
        when(request.getParameter("nameGoal")).thenReturn("Vacation");

        goalServlet.doDelete(request, response);
        writer.flush();

        verify(goalService).deleteGoal("test@example.com", "Vacation");
    }
}
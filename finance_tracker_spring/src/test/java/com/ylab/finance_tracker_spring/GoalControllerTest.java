package com.ylab.finance_tracker_spring;

import com.ylab.finance_tracker_spring.controller.GoalController;
import com.ylab.finance_tracker_spring.domain.service.GoalService;
import com.ylab.finance_tracker_spring.dto.GoalDTO;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import com.ylab.finance_tracker_spring.security.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GoalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoalService goalService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private GoalController goalController;

    private UserDTO testUser;
    private GoalDTO testGoal;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(goalController).build();

        testUser = new UserDTO();
        testUser.setEmail("user@example.com");

        testGoal = GoalDTO.builder()
                .uuid(UUID.randomUUID())
                .email(testUser.getEmail())
                .title("New Car")
                .targetAmount(BigDecimal.valueOf(25000))
                .savedAmount(BigDecimal.ZERO)
                .build();
    }

    @Test
    void getAllGoals_WhenGoalsExist_ReturnsGoals() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getUserGoals(testUser.getEmail()))
                .thenReturn(List.of(testGoal));

        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("New Car"))
                .andExpect(jsonPath("$[0].targetAmount").value(25000));
    }

    @Test
    void getAllGoals_WhenNoGoals_ReturnsNotFound() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getUserGoals(testUser.getEmail())).thenReturn(null);

        mockMvc.perform(get("/goals"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createGoal_WithValidData_ReturnsGoal() throws Exception {
        String requestBody = """
                {
                    "title": "New Car",
                    "targetAmount": 25000
                }
                """;

        mockMvc.perform(post("/goals/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(goalService).createGoal(any(GoalDTO.class));
    }

    @Test
    void createGoal_WithInvalidData_ReturnsBadRequest() throws Exception {
        String invalidRequestBody = """
                {
                    "title": "",
                    "targetAmount": -100
                }
                """;

        mockMvc.perform(post("/goals/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGoal_WithExistingGoal_ReturnsUpdatedGoal() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getGoalByName(testUser.getEmail(), "New Car"))
                .thenReturn(testGoal);

        mockMvc.perform(put("/goals/update")
                        .param("name", "New Car")
                        .param("amount", "5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.savedAmount").value(5000));
    }

    @Test
    void updateGoal_WithNonExistingGoal_ReturnsNotFound() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getGoalByName(testUser.getEmail(), "NonExistingGoal"))
                .thenReturn(null);

        mockMvc.perform(put("/goals/update")
                        .param("name", "NonExistingGoal")
                        .param("amount", "5000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGoal_WithExistingGoal_ReturnsDeletedGoal() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getGoalByName(testUser.getEmail(), "New Car"))
                .thenReturn(testGoal);

        mockMvc.perform(delete("/goals/delete")
                        .param("name", "New Car"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Car"));

        verify(goalService).deleteGoal(testUser.getEmail(), "New Car");
    }

    @Test
    void deleteGoal_WithNonExistingGoal_ReturnsNotFound() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getGoalByName(testUser.getEmail(), "NonExistingGoal"))
                .thenReturn(null);

        mockMvc.perform(delete("/goals/delete")
                        .param("name", "NonExistingGoal"))
                .andExpect(status().isNotFound());
    }
}
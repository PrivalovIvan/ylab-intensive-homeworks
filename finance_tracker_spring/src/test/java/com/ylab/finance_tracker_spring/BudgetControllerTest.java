package com.ylab.finance_tracker_spring;

import com.ylab.finance_tracker_spring.controller.BudgetController;
import com.ylab.finance_tracker_spring.domain.service.BudgetService;
import com.ylab.finance_tracker_spring.dto.BudgetDTO;
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
import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BudgetService budgetService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private BudgetController budgetController;

    private UserDTO testUser;
    private BudgetDTO testBudget;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();

        testUser = new UserDTO();
        testUser.setEmail("user@example.com");

        testBudget = BudgetDTO.builder()
                .uuid(UUID.randomUUID())
                .email(testUser.getEmail())
                .yearMonth(YearMonth.of(2025, 3))
                .budget(BigDecimal.valueOf(1000))
                .spent(BigDecimal.ZERO)
                .build();
    }

    @Test
    void getBudgets_WhenBudgetExists_ReturnsBudget() throws Exception {
        YearMonth yearMonth = YearMonth.of(2025, 3);
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(budgetService.getBudget(testUser.getEmail(), yearMonth))
                .thenReturn(Optional.of(testBudget));

        mockMvc.perform(get("/budgets")
                        .param("yearMonth", "2025-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budget").value(1000));
    }

    @Test
    void getBudgets_WhenBudgetNotExists_ReturnsNotFound() throws Exception {
        YearMonth yearMonth = YearMonth.of(2025, 3);
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(budgetService.getBudget(testUser.getEmail(), yearMonth))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/budgets")
                        .param("yearMonth", "2025-03"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBudget_WithValidData_ReturnsCreatedBudget() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);

        String requestBody = """
                {
                    "yearMonth": "2025-03",
                    "budget": 1000
                }
                """;

        mockMvc.perform(post("/budgets/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(budgetService).createBudget(any(BudgetDTO.class));
    }

    @Test
    void createBudget_WithInvalidData_ReturnsBadRequest() throws Exception {
        String invalidRequestBody = """
                {
                    "yearMonth": null,
                    "budget": -100
                }
                """;

        mockMvc.perform(post("/budgets/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBudget_WithValidData_ReturnsOk() throws Exception {
        YearMonth yearMonth = YearMonth.of(2025, 3);
        BigDecimal expense = BigDecimal.valueOf(100);

        when(authService.getCurrentUser()).thenReturn(testUser);
        doNothing().when(budgetService).addExpense(testUser.getEmail(), yearMonth, expense);

        mockMvc.perform(put("/budgets/update")
                        .param("yearMonth", "2025-03")
                        .param("expense", "100"))
                .andExpect(status().isOk());

        verify(budgetService).addExpense(testUser.getEmail(), yearMonth, expense);
    }

    @Test
    void updateBudget_WithInvalidYearMonth_ReturnsBadRequest() throws Exception {
        mockMvc.perform(put("/budgets/update")
                        .param("yearMonth", "invalid-date")
                        .param("expense", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBudget_WithNegativeExpense_ReturnsBadRequest() throws Exception {
        mockMvc.perform(put("/budgets/update")
                        .param("yearMonth", "2025-03")
                        .param("expense", "-100"))
                .andExpect(status().isBadRequest());
    }
}
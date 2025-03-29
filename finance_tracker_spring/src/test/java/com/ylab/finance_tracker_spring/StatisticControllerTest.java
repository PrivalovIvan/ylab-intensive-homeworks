package com.ylab.finance_tracker_spring;

import com.ylab.finance_tracker_spring.common.TransactionType;
import com.ylab.finance_tracker_spring.controller.StatisticController;
import com.ylab.finance_tracker_spring.domain.service.StatisticsService;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import com.ylab.finance_tracker_spring.security.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StatisticControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StatisticsService statisticsService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private StatisticController statisticController;

    private UserDTO testUser;
    private final LocalDate testFromDate = LocalDate.of(2023, 1, 1);
    private final LocalDate testToDate = LocalDate.of(2023, 12, 31);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statisticController).build();

        testUser = new UserDTO();
        testUser.setEmail("user@example.com");
    }

    @Test
    void getCurrentBalance_ShouldReturnBalance() throws Exception {
        BigDecimal balance = BigDecimal.valueOf(1000.50);
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(statisticsService.getCurrentBalance(testUser.getEmail())).thenReturn(balance);

        mockMvc.perform(get("/statistics/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000.50));

        verify(statisticsService).getCurrentBalance(testUser.getEmail());
    }

    @Test
    void getExpenseForPeriod_ShouldReturnExpense() throws Exception {
        BigDecimal expense = BigDecimal.valueOf(500.75);
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(statisticsService.getTotal(testUser.getEmail(), testFromDate, testToDate, TransactionType.EXPENSE))
                .thenReturn(expense);

        mockMvc.perform(get("/statistics/expense")
                        .param("from", testFromDate.toString())
                        .param("to", testToDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(500.75));

        verify(statisticsService).getTotal(testUser.getEmail(), testFromDate, testToDate, TransactionType.EXPENSE);
    }

    @Test
    void getIncomeForPeriod_ShouldReturnIncome() throws Exception {
        BigDecimal income = BigDecimal.valueOf(1500.25);
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(statisticsService.getTotal(testUser.getEmail(), testFromDate, testToDate, TransactionType.INCOME))
                .thenReturn(income);

        mockMvc.perform(get("/statistics/income")
                        .param("from", testFromDate.toString())
                        .param("to", testToDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1500.25));

        verify(statisticsService).getTotal(testUser.getEmail(), testFromDate, testToDate, TransactionType.INCOME);
    }

    @Test
    void getCostAnalysis_ShouldReturnAnalysis() throws Exception {
        Map<String, BigDecimal> analysis = Map.of(
                "Food", BigDecimal.valueOf(200.50),
                "Transport", BigDecimal.valueOf(100.25)
        );
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(statisticsService.getExpensesByCategory(testUser.getEmail(), testFromDate, testToDate))
                .thenReturn(analysis);

        mockMvc.perform(get("/statistics/analysis")
                        .param("from", testFromDate.toString())
                        .param("to", testToDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Food").value(200.50))
                .andExpect(jsonPath("$.Transport").value(100.25));

        verify(statisticsService).getExpensesByCategory(testUser.getEmail(), testFromDate, testToDate);
    }

    @Test
    void getFinancialReport_ShouldReturnReport() throws Exception {
        String report = "Financial report for period 2023-01-01 to 2023-12-31";
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(statisticsService.generateFinancialReport(testUser.getEmail(), testFromDate, testToDate))
                .thenReturn(report);

        mockMvc.perform(get("/statistics/report")
                        .param("from", testFromDate.toString())
                        .param("to", testToDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(report));

        verify(statisticsService).generateFinancialReport(testUser.getEmail(), testFromDate, testToDate);
    }

    @Test
    void getExpenseForPeriod_WithInvalidDates_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/statistics/expense")
                        .param("from", "invalid-date")
                        .param("to", testToDate.toString()))
                .andExpect(status().isBadRequest());
    }
}
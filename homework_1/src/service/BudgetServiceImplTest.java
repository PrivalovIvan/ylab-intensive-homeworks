package com.ylab.homework_1;

import com.ylab.homework_1.infrastructure.service.BudgetServiceImpl;
import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.domain.repository.BudgetRepository;
import com.ylab.homework_1.domain.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class BudgetServiceImplTest {
    private BudgetRepository budgetRepository;
    private NotificationService notificationService;
    private BudgetServiceImpl budgetService;

    @BeforeEach
    void setUp() {
        budgetRepository = mock(BudgetRepository.class);
        notificationService = mock(NotificationService.class);
        budgetService = new BudgetServiceImpl(budgetRepository, notificationService);
    }

    @Test
    void createBudget_savesNewBudget() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        BigDecimal limit = BigDecimal.valueOf(1000);

        budgetService.createBudget(email, month, limit);

        ArgumentCaptor<Budget> budgetCaptor = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository).save(budgetCaptor.capture());
        Budget savedBudget = budgetCaptor.getValue();
        assertThat(savedBudget.getEmail()).isEqualTo(email);
        assertThat(savedBudget.getYearMonth()).isEqualTo(month);
        assertThat(savedBudget.getBudget()).isEqualTo(limit);
        assertThat(savedBudget.getSpent()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void getBudget_returnsExistingBudget() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        Budget budget = new Budget(email, month, BigDecimal.valueOf(1000), BigDecimal.valueOf(200));
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.of(budget));

        Optional<Budget> result = budgetService.getBudget(email, month);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(budget);
    }

    @Test
    void getBudget_returnsEmptyIfNotFound() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.empty());

        Optional<Budget> result = budgetService.getBudget(email, month);

        assertThat(result).isEmpty();
    }

    @Test
    void addExpense_updatesBudgetAndNotifiesIfExceeded() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        Budget budget = new Budget(email, month, BigDecimal.valueOf(500), BigDecimal.valueOf(400));
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.of(budget));

        budgetService.addExpense(email, month, BigDecimal.valueOf(200));

        ArgumentCaptor<Budget> budgetCaptor = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository).save(budgetCaptor.capture());
        Budget updatedBudget = budgetCaptor.getValue();
        assertThat(updatedBudget.getSpent()).isEqualTo(BigDecimal.valueOf(600)); // 400 + 200
        assertThat(updatedBudget.isExceeded()).isTrue();
    }

    @Test
    void addExpense_doesNothingIfBudgetNotFound() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.empty());

        budgetService.addExpense(email, month, BigDecimal.valueOf(100));

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    void isBudgetExceeded_returnsTrueIfExceeded() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        Budget budget = new Budget(email, month, BigDecimal.valueOf(500), BigDecimal.valueOf(600));
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.of(budget));

        boolean result = budgetService.isBudgetExceeded(email, month);

        assertThat(result).isTrue();
    }

    @Test
    void isBudgetExceeded_returnsFalseIfNotExceeded() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        Budget budget = new Budget(email, month, BigDecimal.valueOf(500), BigDecimal.valueOf(300));
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.of(budget));

        boolean result = budgetService.isBudgetExceeded(email, month);

        assertThat(result).isFalse();
    }

    @Test
    void isBudgetExceeded_returnsFalseIfBudgetNotFound() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.empty());

        boolean result = budgetService.isBudgetExceeded(email, month);

        assertThat(result).isFalse();
    }

    @Test
    void checkAndNotify_sendsNotificationIfBudgetExceeded() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        Budget budget = new Budget(email, month, BigDecimal.valueOf(500), BigDecimal.valueOf(700));
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.of(budget));

        budgetService.checkAndNotify(email, month);

        verify(notificationService).send(email, "Вы превысили бюджет за " + month.getMonth() +
                " месяц, на: " + BigDecimal.valueOf(200)); // 700 - 500 = 200
    }

    @Test
    void checkAndNotify_doesNotNotifyIfBudgetNotExceeded() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        Budget budget = new Budget(email, month, BigDecimal.valueOf(500), BigDecimal.valueOf(400));
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.of(budget));

        budgetService.checkAndNotify(email, month);

        verify(notificationService, never()).send(anyString(), anyString());
    }

    @Test
    void checkAndNotify_doesNotNotifyIfBudgetNotFound() {
        String email = "user@example.com";
        YearMonth month = YearMonth.of(2025, 3);
        when(budgetRepository.findByUserAndMonth(email, month)).thenReturn(Optional.empty());

        budgetService.checkAndNotify(email, month);

        verify(notificationService, never()).send(anyString(), anyString());
    }
}
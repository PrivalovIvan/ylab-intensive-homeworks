package com.ylab.homework_1;

import com.ylab.homework_1.infrastructure.service.TransactionServiceImpl;
import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.domain.repository.BudgetRepository;
import com.ylab.homework_1.domain.repository.TransactionRepository;
import com.ylab.homework_1.domain.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class TransactionServiceImplTest {
    private TransactionRepository transactionRepository;
    private BudgetRepository budgetRepository;
    private BudgetService budgetService;
    private TransactionServiceImpl transactionService;
    private User currentUser;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        budgetRepository = mock(BudgetRepository.class);
        budgetService = mock(BudgetService.class);
        transactionService = new TransactionServiceImpl(transactionRepository, budgetRepository, budgetService);
        currentUser = new User("Test User", "user@example.com", "password", Role.USER);
    }

    @Test
    void createTransaction_expense_updatesExistingBudget() {
        YearMonth currentMonth = YearMonth.now();
        Budget budget = new Budget("user@example.com", currentMonth, BigDecimal.valueOf(1000), BigDecimal.valueOf(500));
        when(budgetRepository.findByUserAndMonth("user@example.com", currentMonth)).thenReturn(Optional.of(budget));

        transactionService.createTransaction(currentUser, TransactionType.EXPENSE, BigDecimal.valueOf(200), "Food", "Lunch");

        verify(transactionRepository).create(any(Transaction.class));
        verify(budgetRepository).save(budget);
        assertThat(budget.getSpent()).isEqualTo(BigDecimal.valueOf(700)); // 500 + 200
        verify(budgetService).checkAndNotify("user@example.com", currentMonth);
    }

    @Test
    void createTransaction_expense_createsNewBudgetIfNotExists() {
        YearMonth currentMonth = YearMonth.now();
        when(budgetRepository.findByUserAndMonth("user@example.com", currentMonth)).thenReturn(Optional.empty());

        transactionService.createTransaction(currentUser, TransactionType.EXPENSE, BigDecimal.valueOf(300), "Transport", "Taxi");

        verify(transactionRepository).create(any(Transaction.class));
        ArgumentCaptor<Budget> budgetCaptor = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository).save(budgetCaptor.capture());
        Budget newBudget = budgetCaptor.getValue();
        assertThat(newBudget.getEmail()).isEqualTo("user@example.com");
        assertThat(newBudget.getYearMonth()).isEqualTo(currentMonth);
        assertThat(newBudget.getBudget()).isEqualTo(BigDecimal.ZERO);
        assertThat(newBudget.getSpent()).isEqualTo(BigDecimal.valueOf(300));
    }

    @Test
    void createTransaction_income_doesNotAffectBudget() {
        transactionService.createTransaction(currentUser, TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly");

        verify(transactionRepository).create(any(Transaction.class));
        verify(budgetRepository, never()).findByUserAndMonth(anyString(), any(YearMonth.class));
        verify(budgetService, never()).checkAndNotify(anyString(), any(YearMonth.class));
    }

    @Test
    void updateTransaction_callsRepositoryUpdate() {
        UUID uuid = UUID.randomUUID();

        transactionService.updateTransaction(currentUser, uuid, BigDecimal.valueOf(400), "Transport", "Taxi");

        verify(transactionRepository).update(uuid, BigDecimal.valueOf(400), "Transport", "Taxi");
    }

    @Test
    void deleteTransaction_callsRepositoryDelete() {
        UUID uuid = UUID.randomUUID();

        transactionService.deleteTransaction(uuid);

        verify(transactionRepository).delete(uuid);
    }

    @Test
    void findAllTransactionUser_returnsTransactions() {
        List<Transaction> transactions = List.of(
                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly")
        );
        when(transactionRepository.findByUserEmail("user@example.com")).thenReturn(transactions);

        List<Transaction> result = transactionService.findAllTransactionUser("user@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void findAllTransactionFilterByDate_filtersCorrectly() {
        LocalDate date = LocalDate.of(2025, 3, 10);
        List<Transaction> transactions = List.of(
                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries") {
                    @Override
                    public LocalDate getDate() {
                        return date;
                    }
                }
        );
        when(transactionRepository.findByUserEmailFilterDate("user@example.com", date)).thenReturn(transactions);

        List<Transaction> result = transactionService.findAllTransactionFilterByDate("user@example.com", date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDate()).isEqualTo(date);
    }

    @Test
    void findAllTransactionFilterByCategory_filtersCorrectly() {
        List<Transaction> transactions = List.of(
                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries")
        );
        when(transactionRepository.findByUserEmailFilterCategory("user@example.com", "Food")).thenReturn(transactions);

        List<Transaction> result = transactionService.findAllTransactionFilterByCategory("user@example.com", "Food");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("Food");
    }

    @Test
    void findAllTransactionFilterByType_filtersCorrectly() {
        List<Transaction> transactions = List.of(
                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly")
        );
        when(transactionRepository.findByUserEmailFilterType("user@example.com", TransactionType.INCOME)).thenReturn(transactions);

        List<Transaction> result = transactionService.findAllTransactionFilterByType("user@example.com", TransactionType.INCOME);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo(TransactionType.INCOME);
    }
}
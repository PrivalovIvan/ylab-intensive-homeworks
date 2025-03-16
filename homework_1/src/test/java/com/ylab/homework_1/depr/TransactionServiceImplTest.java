//package com.ylab.homework_1;
//
//import com.ylab.homework_1.common.Role;
//import com.ylab.homework_1.common.TransactionType;
//import com.ylab.homework_1.domain.model.Budget;
//import com.ylab.homework_1.domain.model.Transaction;
//import com.ylab.homework_1.domain.model.User;
//import com.ylab.homework_1.domain.repository.BudgetRepository;
//import com.ylab.homework_1.domain.repository.TransactionRepository;
//import com.ylab.homework_1.domain.service.BudgetService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//class TransactionServiceImplTest {
//    private TransactionRepository transactionRepository;
//    private BudgetRepository budgetRepository;
//    private BudgetService budgetService;
//    private TransactionServiceImpl transactionService;
//    private User currentUser;
//
//    @BeforeEach
//    void setUp() {
//        transactionRepository = Mockito.mock(TransactionRepository.class);
//        budgetRepository = Mockito.mock(BudgetRepository.class);
//        budgetService = Mockito.mock(BudgetService.class);
//        transactionService = new TransactionServiceImpl(transactionRepository, budgetRepository, budgetService);
//        currentUser = new User("Test User", "user@example.com", "password", Role.USER);
//    }
//
//    @Test
//    void createTransaction_expense_updatesExistingBudget() {
//        YearMonth currentMonth = YearMonth.now();
//        Budget budget = new Budget("user@example.com", currentMonth, BigDecimal.valueOf(1000), BigDecimal.valueOf(500));
//        Mockito.when(budgetRepository.findByUserAndMonth("user@example.com", currentMonth)).thenReturn(Optional.of(budget));
//
//        transactionService.createTransaction(currentUser, TransactionType.EXPENSE, BigDecimal.valueOf(200), "Food", "Lunch");
//
//        Mockito.verify(transactionRepository).create(ArgumentMatchers.any(Transaction.class));
//        Mockito.verify(budgetRepository).save(budget);
//        Assertions.assertThat(budget.getSpent()).isEqualTo(BigDecimal.valueOf(700)); // 500 + 200
//        Mockito.verify(budgetService).checkAndNotify("user@example.com", currentMonth);
//    }
//
//    @Test
//    void createTransaction_expense_createsNewBudgetIfNotExists() {
//        YearMonth currentMonth = YearMonth.now();
//        Mockito.when(budgetRepository.findByUserAndMonth("user@example.com", currentMonth)).thenReturn(Optional.empty());
//
//        transactionService.createTransaction(currentUser, TransactionType.EXPENSE, BigDecimal.valueOf(300), "Transport", "Taxi");
//
//        Mockito.verify(transactionRepository).create(ArgumentMatchers.any(Transaction.class));
//        ArgumentCaptor<Budget> budgetCaptor = ArgumentCaptor.forClass(Budget.class);
//        Mockito.verify(budgetRepository).save(budgetCaptor.capture());
//        Budget newBudget = budgetCaptor.getValue();
//        Assertions.assertThat(newBudget.getEmail()).isEqualTo("user@example.com");
//        Assertions.assertThat(newBudget.getYearMonth()).isEqualTo(currentMonth);
//        Assertions.assertThat(newBudget.getBudget()).isEqualTo(BigDecimal.ZERO);
//        Assertions.assertThat(newBudget.getSpent()).isEqualTo(BigDecimal.valueOf(300));
//    }
//
//    @Test
//    void createTransaction_income_doesNotAffectBudget() {
//        transactionService.createTransaction(currentUser, TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly");
//
//        Mockito.verify(transactionRepository).create(ArgumentMatchers.any(Transaction.class));
//        Mockito.verify(budgetRepository, Mockito.never()).findByUserAndMonth(ArgumentMatchers.anyString(), ArgumentMatchers.any(YearMonth.class));
//        Mockito.verify(budgetService, Mockito.never()).checkAndNotify(ArgumentMatchers.anyString(), ArgumentMatchers.any(YearMonth.class));
//    }
//
//    @Test
//    void updateTransaction_callsRepositoryUpdate() {
//        UUID uuid = UUID.randomUUID();
//
//        transactionService.updateTransaction(currentUser, uuid, BigDecimal.valueOf(400), "Transport", "Taxi");
//
//        Mockito.verify(transactionRepository).update(uuid, BigDecimal.valueOf(400), "Transport", "Taxi");
//    }
//
//    @Test
//    void deleteTransaction_callsRepositoryDelete() {
//        UUID uuid = UUID.randomUUID();
//
//        transactionService.deleteTransaction(uuid);
//
//        Mockito.verify(transactionRepository).delete(uuid);
//    }
//
//    @Test
//    void findAllTransactionUser_returnsTransactions() {
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly")
//        );
//        Mockito.when(transactionRepository.findByUserEmail("user@example.com")).thenReturn(transactions);
//
//        List<Transaction> result = transactionService.findAllTransactionUser("user@example.com");
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getEmail()).isEqualTo("user@example.com");
//    }
//
//    @Test
//    void findAllTransactionFilterByDate_filtersCorrectly() {
//        LocalDate date = LocalDate.of(2025, 3, 10);
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries") {
//                    @Override
//                    public LocalDate getDate() {
//                        return date;
//                    }
//                }
//        );
//        Mockito.when(transactionRepository.findByUserEmailFilterDate("user@example.com", date)).thenReturn(transactions);
//
//        List<Transaction> result = transactionService.findAllTransactionFilterByDate("user@example.com", date);
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getDate()).isEqualTo(date);
//    }
//
//    @Test
//    void findAllTransactionFilterByCategory_filtersCorrectly() {
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries")
//        );
//        Mockito.when(transactionRepository.findByUserEmailFilterCategory("user@example.com", "Food")).thenReturn(transactions);
//
//        List<Transaction> result = transactionService.findAllTransactionFilterByCategory("user@example.com", "Food");
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getCategory()).isEqualTo("Food");
//    }
//
//    @Test
//    void findAllTransactionFilterByType_filtersCorrectly() {
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly")
//        );
//        Mockito.when(transactionRepository.findByUserEmailFilterType("user@example.com", TransactionType.INCOME)).thenReturn(transactions);
//
//        List<Transaction> result = transactionService.findAllTransactionFilterByType("user@example.com", TransactionType.INCOME);
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getType()).isEqualTo(TransactionType.INCOME);
//    }
//}
//package com.ylab.homework_1;
//
//import com.ylab.homework_1.common.TransactionType;
//import com.ylab.homework_1.domain.model.Transaction;
//import com.ylab.homework_1.domain.service.TransactionService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//class StatisticsServiceImplTest {
//    @Mock
//    private TransactionService transactionService;
//    private StatisticsServiceImpl statisticsService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        statisticsService = new StatisticsServiceImpl(transactionService);
//    }
//
//    @Test
//    void getCurrentBalance_calculatesBalanceCorrectly() {
//        String email = "user@example.com";
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly"),
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries"),
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(500), "Bonus", "Extra")
//        );
//        Mockito.when(transactionService.findAllTransactionUser(email)).thenReturn(transactions);
//
//        BigDecimal balance = statisticsService.getCurrentBalance(email);
//
//        Assertions.assertThat(balance).isEqualTo(BigDecimal.valueOf(1200)); // 1000 + 500 - 300 = 1200
//    }
//
//    @Test
//    void getCurrentBalance_returnsZeroForEmptyTransactions() {
//        String email = "user@example.com";
//        Mockito.when(transactionService.findAllTransactionUser(email)).thenReturn(List.of());
//
//        BigDecimal balance = statisticsService.getCurrentBalance(email);
//
//        Assertions.assertThat(balance).isEqualTo(BigDecimal.ZERO);
//    }
//
//    @Test
//    void getTotal_calculatesIncomeInRange() {
//        String email = "user@example.com";
//        LocalDate from = LocalDate.of(2025, 3, 1);
//        LocalDate to = LocalDate.of(2025, 3, 31);
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly"),
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries"),
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(500), "Bonus", "Extra") // Outside range
//        );
//        Mockito.when(transactionService.findAllTransactionUser(email)).thenReturn(transactions);
//
//        BigDecimal totalIncome = statisticsService.getTotal(email, from, to, TransactionType.INCOME);
//
//        Assertions.assertThat(totalIncome).isEqualTo(BigDecimal.valueOf(1500)); // Only 1000 within range
//    }
//
//    @Test
//    void getTotal_returnsZeroForNoMatchingTransactions() {
//        String email = "user@example.com";
//        LocalDate from = LocalDate.of(2025, 3, 1);
//        LocalDate to = LocalDate.of(2025, 3, 31);
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries")
//        );
//        Mockito.when(transactionService.findAllTransactionUser(email)).thenReturn(transactions);
//
//        BigDecimal totalIncome = statisticsService.getTotal(email, from, to, TransactionType.INCOME);
//
//        Assertions.assertThat(totalIncome).isEqualTo(BigDecimal.ZERO);
//    }
//
//    @Test
//    void getExpensesByCategory_groupsExpensesCorrectly() {
//        String email = "user@example.com";
//        LocalDate from = LocalDate.of(2025, 3, 1);
//        LocalDate to = LocalDate.of(2025, 3, 31);
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries"),
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(200), "Food", "Dinner"),
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(100), "Transport", "Taxi"),
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly"),
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(50), "Food", "Lunch") // Outside range
//        );
//        Mockito.when(transactionService.findAllTransactionUser(email)).thenReturn(transactions);
//
//        Map<String, BigDecimal> expensesByCategory = statisticsService.getExpensesByCategory(email, from, to);
//
//        Assertions.assertThat(expensesByCategory).hasSize(2);
//        Assertions.assertThat(expensesByCategory.get("Food")).isEqualTo(BigDecimal.valueOf(550)); // 300 + 200
//        Assertions.assertThat(expensesByCategory.get("Transport")).isEqualTo(BigDecimal.valueOf(100));
//    }
//
//    @Test
//    void getExpensesByCategory_returnsEmptyMapForNoExpenses() {
//        String email = "user@example.com";
//        LocalDate from = LocalDate.of(2025, 3, 1);
//        LocalDate to = LocalDate.of(2025, 3, 31);
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly")
//        );
//        Mockito.when(transactionService.findAllTransactionUser(email)).thenReturn(transactions);
//
//        Map<String, BigDecimal> expensesByCategory = statisticsService.getExpensesByCategory(email, from, to);
//
//        Assertions.assertThat(expensesByCategory).isEmpty();
//    }
//
//    @Test
//    void generateFinancialReport_createsCorrectReport() {
//        String email = "user@example.com";
//        LocalDate from = LocalDate.of(2025, 3, 1);
//        LocalDate to = LocalDate.of(2025, 3, 31);
//        List<Transaction> transactions = List.of(
//                new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly"),
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries"),
//                new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(100), "Transport", "Taxi")
//        );
//        Mockito.when(transactionService.findAllTransactionUser(email)).thenReturn(transactions);
//
//        String report = statisticsService.generateFinancialReport(email, from, to);
//
//        Assertions.assertThat(report).contains("Balance: 600"); // 1000 - 300 - 100
//        Assertions.assertThat(report).contains("Income: 1000");
//        Assertions.assertThat(report).contains("Expense: 400");
//        Assertions.assertThat(report).contains("Food: 300");
//        Assertions.assertThat(report).contains("Transport: 100");
//    }
//
//    @Test
//    void generateFinancialReport_handlesEmptyTransactions() {
//        String email = "user@example.com";
//        LocalDate from = LocalDate.of(2025, 3, 1);
//        LocalDate to = LocalDate.of(2025, 3, 31);
//        Mockito.when(transactionService.findAllTransactionUser(email)).thenReturn(List.of());
//
//        String report = statisticsService.generateFinancialReport(email, from, to);
//
//        Assertions.assertThat(report).contains("Balance: 0");
//        Assertions.assertThat(report).contains("Income: 0");
//        Assertions.assertThat(report).contains("Expense: 0");
//        Assertions.assertThat(report).contains("Category: \n");
//    }
//}

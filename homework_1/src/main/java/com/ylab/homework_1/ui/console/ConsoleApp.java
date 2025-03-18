package com.ylab.homework_1.ui.console;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.usecase.dto.BudgetDTO;
import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.usecase.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ConsoleApp {
    private final UserService userService;
    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private final GoalService goalService;
    private final StatisticsService statisticsService;
    private final AdministrationService administrationService;

    private UserDTO currentUser;
    Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("""
                    1. Register
                    2. Login
                    
                    0. Exit
                    """);
            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            switch (choice) {
                case 1 -> register();
                case 2 -> {
                    try {
                        if (currentUser == null) login();
                        else menuUser();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }


    private void register() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            userService.register(new UserDTO(null, name, email, password, Role.USER));
        } catch (IllegalArgumentException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            currentUser = userService.login(email, password);
            if (currentUser.getRole() == Role.USER) menuUser();
            else if (currentUser.getRole() == Role.ADMIN) menuAdmin();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void menuAdmin() {
        while (true) {
            System.out.println("""
                    === ADMIN MENU ===
                    1. Get list of users
                    2. Get transactions of user
                    3. Delete user
                    
                    0. Exit
                    """);

            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            try {
                switch (choice) {
                    case 1 -> {
                        System.out.println("Users: ");
                        System.out.println(administrationService.findAllUsers());
                    }
                    case 2 -> {
                        System.out.println("Email users: ");
                        String email = scanner.nextLine();
                        System.out.println("Transactions: ");
                        try {
                            System.out.println(administrationService.findAllTransactionsOfUsers(email));
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case 3 -> {
                        System.out.println("Email user: ");
                        String email = scanner.nextLine();
                        administrationService.deleteUser(email);
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void menuUser() {
        while (currentUser != null) {
            System.out.println("""
                    ====== M E N U ======
                    1. Profile settings
                    2. Financial management
                    3. Budget management
                    4. Goal management
                    5. Statistics and analytics
                    
                    0. Exit
                    """);

            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            try {
                switch (choice) {
                    case 1 -> profileSettings();
                    case 2 -> financialManagement();
                    case 3 -> budgetManagement();
                    case 4 -> goalManagement();
                    case 5 -> statisticsAndAnalytics();

                    case 0 -> {
                        currentUser = null;
                        return;
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // region Profile settings
    private void profileSettings() {
        while (true) {
            System.out.println("""
                    1. Change name
                    2. Change email
                    3. Change password
                    4. Delete profile
                    5. View profile
                    
                    0. Exit
                    """);
            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            try {

                switch (choice) {
                    case 1 -> {
                        System.out.print("New name: ");
                        String name = scanner.nextLine();
                        currentUser = userService.updateUser(currentUser.getUuid(), name, null, null);
                        System.out.println("Name update successfully");
                    }
                    case 2 -> {
                        System.out.print("New email: ");
                        String email = scanner.nextLine();
                        currentUser = userService.updateUser(currentUser.getUuid(), null, email, null);
                        System.out.println("Email update successfully");
                    }
                    case 3 -> {
                        System.out.print("New password: ");
                        String password = scanner.nextLine();
                        currentUser = userService.updateUser(currentUser.getUuid(), null, null, password);
                        System.out.println("Password update successfully");
                    }
                    case 4 -> {
                        System.out.println("""
                                Are you sure you want to delete your account?
                                1. Yes
                                2. No
                                """);
                        int delete = scanner.nextInt();
                        scanner.nextLine();
                        if (delete == 1) {
                            userService.delete(currentUser.getEmail());
                            currentUser = null;
                            return;
                        }
                    }
                    case 5 -> System.out.println(userService.findByUuid(currentUser.getUuid()));
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //endregion
//region Financial management
    private void financialManagement() {
        while (true) {
            System.out.println("""
                    1. Create transaction
                    2. Update transaction
                    3. Delete transaction
                    4. View transactions
                    0. Exit
                    """);
            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            try {
                switch (choice) {
                    case 1 -> createTransaction();
                    case 2 -> updateTransaction();
                    case 3 -> deleteTransaction();
                    case 4 -> viewTransaction();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createTransaction() {
        System.out.println("""
                Type transaction:
                    1. INCOME
                    2. EXPENSE
                """);
        int typeTransaction = scanner.nextInt();
        scanner.nextLine();
        TransactionType type = TransactionType.values()[typeTransaction - 1];
        String nameGoal = null;
        if (type == TransactionType.INCOME) {
            System.out.print("Name of the replenishment goal (optional, press Enter to skip): ");
            nameGoal = scanner.nextLine();
            if (nameGoal.isEmpty()) nameGoal = null;
            else {
                try {
                    goalService.getGoalByName(currentUser.getEmail(), nameGoal);
                } catch (IllegalArgumentException e) {
                    System.out.println("Goal not found, transaction will be created without goal.");
                    nameGoal = null;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.print("Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        LocalDate date = LocalDate.now();

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .email(currentUser.getEmail())
                .type(type)
                .amount(amount)
                .category(category)
                .nameGoal(nameGoal)
                .date(date)
                .description(description)
                .build();

        try {
            transactionService.createTransaction(transactionDTO);
            if (type == TransactionType.EXPENSE) {
                budgetService.processExpense(transactionDTO);
            } else if (type == TransactionType.INCOME && nameGoal != null) {
                goalService.processIncome(transactionDTO);
            }
            System.out.println("Transaction created successfully");
        } catch (SQLException e) {
            System.out.println("Failed to create transaction: " + e.getMessage());
        }
    }

    private void updateTransaction() {
        System.out.print("Id transaction: ");
        UUID id = UUID.fromString(scanner.nextLine());
        System.out.print("New Amount: ");
        BigDecimal newAmount = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.print("New Category: ");
        String newCategory = scanner.nextLine();
        System.out.print("New Description: ");
        String newDescription = scanner.nextLine();

        try {
            transactionService.findAllTransactionUser(currentUser.getEmail())
                    .stream()
                    .filter(t -> t.getUuid().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
            transactionService.updateAmount(currentUser.getEmail(), id, newAmount);
            transactionService.updateCategory(currentUser.getEmail(), id, newCategory);
            transactionService.updateDescription(currentUser.getEmail(), id, newDescription);
            System.out.println("Transaction updated successfully");
        } catch (SQLException e) {
            System.out.println("Failed to update transaction: " + e.getMessage());
        }
    }

    private void deleteTransaction() {
        System.out.print("Id transaction: ");
        UUID idTransaction = UUID.fromString(scanner.nextLine());
        scanner.nextLine();
        try {
            transactionService.deleteTransaction(currentUser.getEmail(), idTransaction);
            System.out.println("Transaction deleted successfully");
        } catch (SQLException e) {
            System.out.println("Failed to delete transaction: " + e.getMessage());
        }
    }

    private void viewTransaction() {
        while (true) {
            System.out.println("""
                    1. Find all transactions
                    2. Find transaction by date
                    3. Find transaction by category
                    4. Find transaction by type
                    0. Exit
                    """);
            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            try {
                switch (choice) {
                    case 1 -> {
                        List<TransactionDTO> transactions = transactionService.findAllTransactionUser(currentUser.getEmail());
                        if (transactions.isEmpty()) System.out.println("No transactions found");
                        else transactions.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Year: ");
                        int year = scanner.nextInt();
                        System.out.print("Month: ");
                        int month = scanner.nextInt();
                        System.out.print("Day: ");
                        int day = scanner.nextInt();
                        scanner.nextLine();
                        LocalDate date = LocalDate.of(year, month, day);
                        List<TransactionDTO> transactions = transactionService.findAllTransactionFilterByDate(currentUser.getEmail(), date);
                        if (transactions.isEmpty()) System.out.println("No transactions found");
                        else transactions.forEach(System.out::println);
                    }
                    case 3 -> {
                        System.out.print("Category: ");
                        String category = scanner.nextLine();
                        List<TransactionDTO> transactions = transactionService.findAllTransactionFilterByCategory(currentUser.getEmail(), category);
                        if (transactions.isEmpty()) System.out.println("No transactions found");
                        else transactions.forEach(System.out::println);
                    }
                    case 4 -> {
                        System.out.print("""
                                Type transaction:
                                    1. INCOME
                                    2. EXPENSE
                                """);
                        int type = scanner.nextInt();
                        scanner.nextLine();
                        List<TransactionDTO> transactions = transactionService.findAllTransactionFilterByType(
                                currentUser.getEmail(), type == 1 ? TransactionType.INCOME : TransactionType.EXPENSE);
                        if (transactions.isEmpty()) System.out.println("No transactions found");
                        else transactions.forEach(System.out::println);
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (SQLException e) {
                System.out.println("Failed to view transactions: " + e.getMessage());
            }
        }
    }

    //endregion
//region Budget management
    private void budgetManagement() {
        while (true) {
            System.out.println("""
                    1. Create budget
                    2. Add expense
                    3. Check budget
                    4. View budget
                    
                    0. Exit
                    """);

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> createBudget();
                case 2 -> addExpense();
                case 3 -> checkBudget();
                case 4 -> viewBudget();

                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void createBudget() {
        System.out.print("Enter budget month (YYYY-MM): ");
        YearMonth yearMonth = YearMonth.parse(scanner.nextLine());
        System.out.print("Enter budget limit: ");
        BigDecimal budgetLimit = scanner.nextBigDecimal();
        scanner.nextLine();
        BudgetDTO budgetDTO = BudgetDTO.builder()
                .email(currentUser.getEmail())
                .yearMonth(yearMonth)
                .budget(budgetLimit)
                .spent(BigDecimal.ZERO)
                .build();
        try {
            budgetService.createBudget(budgetDTO);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addExpense() {
        System.out.print("Enter budget month (YYYY-MM): ");
        YearMonth yearMonth = YearMonth.parse(scanner.nextLine());
        System.out.print("Expense: ");
        BigDecimal expense = scanner.nextBigDecimal();
        scanner.nextLine();
        try {
            budgetService.addExpense(currentUser.getEmail(), yearMonth, expense);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkBudget() {
        System.out.println("Enter budget month (YYYY-MM): ");
        YearMonth yearMonth = YearMonth.parse(scanner.nextLine());
        try {
            System.out.println("Budget is exceeded: " + budgetService.isBudgetExceeded(currentUser.getEmail(), yearMonth));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewBudget() {
        System.out.println("Enter budget month (YYYY-MM): ");
        YearMonth yearMonth = YearMonth.parse(scanner.nextLine());
        try {
            budgetService.getBudget(currentUser.getEmail(), yearMonth)
                    .ifPresentOrElse(budget -> System.out.println("Budget: " + budget),
                            () -> System.out.println("Budget not found for " + yearMonth));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //endregion
//region Goal management
    private void goalManagement() {
        while (true) {
            System.out.println("""
                    1. Create goal
                    2. View goal
                    3. Update goal
                    4. Delete goal
                    0. Exit
                    """);

            int choice = scanner.nextInt();
            scanner.nextLine();
            try {
                switch (choice) {
                    case 1 -> createGoal();
                    case 2 -> viewGoal();
                    case 3 -> updateGoal();
                    case 4 -> deleteGoal();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createGoal() {
        System.out.print("Name goal: ");
        String name = scanner.nextLine();
        System.out.print("Amount goal: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        GoalDTO goalDTO = GoalDTO.builder()
                .email(currentUser.getEmail())
                .title(name)
                .targetAmount(amount)
                .savedAmount(BigDecimal.ZERO)
                .build();
        try {
            goalService.createGoal(goalDTO);
            System.out.println("Goal created successfully");
        } catch (SQLException e) {
            System.out.println("Failed to create goal: " + e.getMessage());
        }
    }

    private void viewGoal() {
        try {
            List<GoalDTO> goals = goalService.getUserGoals(currentUser.getEmail());
            if (goals.isEmpty()) {
                System.out.println("No goals found for " + currentUser.getEmail());
            } else {
                goals.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Failed to view goals: " + e.getMessage());
        }
    }

    private void updateGoal() {
        System.out.print("Name goal: ");
        String name = scanner.nextLine();
        System.out.print("Add amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        try {
            GoalDTO existingGoal = goalService.getGoalByName(currentUser.getEmail(), name);
            BigDecimal newSavedAmount = existingGoal.getSavedAmount().add(amount);
            GoalDTO updatedGoal = GoalDTO.builder()
                    .email(currentUser.getEmail())
                    .title(name)
                    .targetAmount(existingGoal.getTargetAmount())
                    .savedAmount(newSavedAmount)
                    .build();
            goalService.updateGoal(updatedGoal);
            System.out.println("Goal updated successfully");
        } catch (SQLException e) {
            System.out.println("Failed to update goal: " + e.getMessage());
        }
    }

    private void deleteGoal() {
        System.out.print("Name goal: ");
        String name = scanner.nextLine();
        try {
            goalService.deleteGoal(currentUser.getEmail(), name);
            System.out.println("Goal deleted successfully");
        } catch (SQLException e) {
            System.out.println("Failed to delete goal: " + e.getMessage());
        }
    }

    //endregion
//region Statistics and analytics
    private void statisticsAndAnalytics() {
        while (true) {
            System.out.println("""
                    1. Current balance
                    2. Income for the period
                    3. Expenses for the period
                    4. Cost analysis by category
                    5. Financial report
                    
                    0. Exit
                    """);

            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            switch (choice) {
                case 1 -> currentBalance();
                case 2 -> incomeForThePeriod();
                case 3 -> expenseForThePeriod();
                case 4 -> costAnalysisByCategory();
                case 5 -> financialReport();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void currentBalance() {
        try {
            System.out.println("Current balance: " + statisticsService.getCurrentBalance(currentUser.getEmail()));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void incomeForThePeriod() {
        System.out.print("Period from (YYYY-MM-DD): ");
        LocalDate dateFrom = LocalDate.parse(scanner.nextLine());
        System.out.print("Period to (YYYY-MM-DD): ");
        LocalDate dateTo = LocalDate.parse(scanner.nextLine());
        try {
            System.out.println("Income for the period: " + statisticsService.getTotal(currentUser.getEmail(), dateFrom, dateTo, TransactionType.INCOME));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void expenseForThePeriod() {
        System.out.print("Period from (YYYY-MM-DD): ");
        LocalDate dateFrom = LocalDate.parse(scanner.nextLine());
        System.out.print("Period to (YYYY-MM-DD): ");
        LocalDate dateTo = LocalDate.parse(scanner.nextLine());
        try {
            System.out.println("Income for the period: " + statisticsService.getTotal(currentUser.getEmail(), dateFrom, dateTo, TransactionType.EXPENSE));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void costAnalysisByCategory() {
        System.out.print("Period from (YYYY-MM-DD): ");
        LocalDate dateFrom = LocalDate.parse(scanner.nextLine());
        System.out.print("Period to (YYYY-MM-DD): ");
        LocalDate dateTo = LocalDate.parse(scanner.nextLine());
        System.out.println("Cost analysis by category: ");
        Map<String, BigDecimal> expensesByCategory = null;
        try {
            expensesByCategory = statisticsService.getExpensesByCategory(currentUser.getEmail(), dateFrom, dateTo);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        assert expensesByCategory != null;
        for (Map.Entry<String, BigDecimal> entry : expensesByCategory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void financialReport() {
        System.out.print("Period from (YYYY-MM-DD): ");
        LocalDate dateFrom = LocalDate.parse(scanner.nextLine());
        System.out.print("Period to (YYYY-MM-DD): ");
        LocalDate dateTo = LocalDate.parse(scanner.nextLine());
        System.out.println("Financial report: ");
        try {
            System.out.println(statisticsService.generateFinancialReport(currentUser.getEmail(), dateFrom, dateTo));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
//endregion

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Ошибка очистки консоли.");
        }
    }
}

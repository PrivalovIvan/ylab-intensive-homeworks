package com.ylab.homework_1.ui.console;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.domain.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
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

    private User currentUser;
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
            switch (choice) {
                case 1 -> register();
                case 2 -> {
                    if (currentUser == null) login();
                    else profileSettings();
                }
                case 0 -> {
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
            userService.register(name, email, password, Role.USER);
        } catch (IllegalArgumentException e) {
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
            switch (choice) {
                case 1 -> {
                    System.out.println("Users: ");
                    System.out.println(administrationService.findAllUsers());
                }
                case 2 -> {
                    System.out.println("Email users: ");
                    String email = scanner.nextLine();
                    System.out.println("Transactions: ");
                    System.out.println(administrationService.findAllTransactionsOfUsers(email));
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
        }
    }

    public void menuUser() {
        while (true) {
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
            switch (choice) {
                case 1 -> profileSettings();
                case 2 -> financialManagement();
                case 3 -> budgetManagement();
                case 4 -> goalManagement();
                case 5 -> statisticsAndAnalytics();

                case 0 -> {
                    return;
                }
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
            switch (choice) {
                case 1 -> {
                    System.out.print("New name: ");
                    String name = scanner.nextLine();
                    currentUser = userService.changeName(currentUser, name);
                    System.out.println("Name update successfully");
                }
                case 2 -> {
                    System.out.print("New email: ");
                    String email = scanner.nextLine();
                    currentUser = userService.changeEmail(currentUser, email);
                    System.out.println("Email update successfully");
                }
                case 3 -> {
                    System.out.print("New password: ");
                    String password = scanner.nextLine();
                    currentUser = userService.changePassword(currentUser, password);
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
                        userService.delete(currentUser);
                        currentUser = null;
                    }
                }
                case 5 -> System.out.println(userService.findByEmail(currentUser.getEmail()));
                case 0 -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid choice");
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
                    4. View transaction
                    
                    0. Exit
                    """);
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> createTransaction();
                case 2 -> updateTransaction();
                case 3 -> deleteTransaction();
                case 4 -> viewTransaction();

                case 0 -> {
                    return;
                }
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
        System.out.print("Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();


        transactionService.createTransaction(currentUser,
                typeTransaction == 1 ? TransactionType.INCOME : TransactionType.EXPENSE,
                amount,
                category,
                description
        );
    }

    private void updateTransaction() {
        System.out.print("Id transaction: ");
        String idTransaction = scanner.nextLine();
        System.out.print("New Amount: ");
        BigDecimal newAmount = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("New Category: ");
        String newCategory = scanner.nextLine();

        System.out.print("New Description: ");
        String newDescription = scanner.nextLine();

        transactionService.updateTransaction(
                currentUser,
                UUID.fromString(idTransaction),
                newAmount,
                newCategory,
                newDescription
        );
    }

    private void deleteTransaction() {
        System.out.print("Id transaction: ");
        String idTransaction = scanner.nextLine();
        transactionService.deleteTransaction(UUID.fromString(idTransaction));
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
            switch (choice) {
                case 1 -> System.out.println(transactionService.findAllTransactionUser(currentUser.getEmail()));
                case 2 -> {
                    System.out.print("Year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Month: ");
                    int month = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Day: ");
                    int day = scanner.nextInt();
                    scanner.nextLine();
                    LocalDate date = LocalDate.of(year, month, day);
                    System.out.println(transactionService.findAllTransactionFilterByDate(currentUser.getEmail(), date));
                }
                case 3 -> {
                    System.out.print("Category: ");
                    String category = scanner.nextLine();
                    System.out.println(transactionService.findAllTransactionFilterByCategory(currentUser.getEmail(), category));
                }
                case 4 -> {
                    System.out.print("""
                            Type transaction:
                                1. INCOME
                                2. EXPENSE
                            """);
                    int type = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(transactionService.findAllTransactionFilterByType(currentUser.getEmail(),
                            type == 1 ? TransactionType.INCOME : TransactionType.EXPENSE));
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
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
            }
        }
    }

    private void createBudget() {
        System.out.print("Enter budget month (YYYY-MM): ");
        YearMonth yearMonth = YearMonth.parse(scanner.nextLine());
        System.out.print("Enter budget limit: ");
        BigDecimal budgetLimit = scanner.nextBigDecimal();
        budgetService.createBudget(currentUser.getEmail(), yearMonth, budgetLimit);
    }

    private void addExpense() {
        System.out.print("Enter budget month (YYYY-MM): ");
        YearMonth yearMonth = YearMonth.parse(scanner.nextLine());
        System.out.print("Expense: ");
        BigDecimal expense = scanner.nextBigDecimal();
        budgetService.addExpense(currentUser.getEmail(), yearMonth, expense);
    }

    private void checkBudget() {
        System.out.println("Enter budget month (YYYY-MM): ");
        YearMonth yearMonth = YearMonth.parse(scanner.nextLine());
        System.out.println("Budget is exceeded: " + budgetService.isBudgetExceeded(currentUser.getEmail(), yearMonth));
    }

    private void viewBudget() {
        System.out.println("Enter budget month (YYYY-MM): ");
        YearMonth yearMonth = YearMonth.parse(scanner.nextLine());
        budgetService.getBudget(currentUser.getEmail(), yearMonth)
                .ifPresentOrElse(budget -> System.out.println("Budget: " + budget),
                        () -> System.out.println("Budget not found for " + yearMonth));
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
            switch (choice) {
                case 1 -> createGoal();
                case 2 -> viewGoal();
                case 3 -> updateGoal();
                case 4 -> deleteGoal();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void createGoal() {
        System.out.println("Name goal: ");
        String name = scanner.nextLine();
        System.out.println("Amount goal: ");
        BigDecimal amount = scanner.nextBigDecimal();
        goalService.setGoal(currentUser.getEmail(), name, amount);
    }

    private void viewGoal() {
        System.out.println(goalService.getUserGoals(currentUser.getEmail()));
    }

    private void updateGoal() {
        System.out.println("Name goal: ");
        String name = scanner.nextLine();
        System.out.println("Add amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        goalService.updateGoalProgress(currentUser.getEmail(), name, amount);
    }

    private void deleteGoal() {
        System.out.println("Name goal: ");
        String name = scanner.nextLine();
        goalService.deleteGoal(currentUser.getEmail(), name);
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
        System.out.println("Current balance: " + statisticsService.getCurrentBalance(currentUser.getEmail()));
    }

    private void incomeForThePeriod() {
        System.out.print("Period from (YYYY-MM-DD): ");
        LocalDate dateFrom = LocalDate.parse(scanner.nextLine());
        System.out.print("Period to (YYYY-MM-DD): ");
        LocalDate dateTo = LocalDate.parse(scanner.nextLine());
        System.out.println("Income for the period: " + statisticsService.getTotal(currentUser.getEmail(), dateFrom, dateTo, TransactionType.INCOME));
    }

    private void expenseForThePeriod() {
        System.out.print("Period from (YYYY-MM-DD): ");
        LocalDate dateFrom = LocalDate.parse(scanner.nextLine());
        System.out.print("Period to (YYYY-MM-DD): ");
        LocalDate dateTo = LocalDate.parse(scanner.nextLine());
        System.out.println("Income for the period: " + statisticsService.getTotal(currentUser.getEmail(), dateFrom, dateTo, TransactionType.EXPENSE));
    }

    private void costAnalysisByCategory() {
        System.out.print("Period from (YYYY-MM-DD): ");
        LocalDate dateFrom = LocalDate.parse(scanner.nextLine());
        System.out.print("Period to (YYYY-MM-DD): ");
        LocalDate dateTo = LocalDate.parse(scanner.nextLine());
        System.out.println("Cost analysis by category: ");
        var expensesByCategory = statisticsService.getExpensesByCategory(currentUser.getEmail(), dateFrom, dateTo);

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
        System.out.println(statisticsService.generateFinancialReport(currentUser.getEmail(), dateFrom, dateTo));
    }
    //endregion
}

package com.ylab.homework_1.ui;

import com.ylab.homework_1.app.AppConfig;
import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.ui.controller.*;
import com.ylab.homework_1.usecase.dto.UserDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static com.ylab.homework_1.ui.controller.ClearConsoleUtil.clearConsole;

@Data
@RequiredArgsConstructor
public class ConsoleApp {
    public static UserDTO currentUser;
    private final Scanner scanner = new Scanner(System.in);
    private final AppConfig config;
    private final UserController userController;
    private final BudgetController budgetController;
    private final GoalController goalController;
    private final AdministrationController administrationController;
    private final TransactionController transactionController;
    private final StatisticController statisticController;

    public ConsoleApp() throws Exception {
        config = AppConfig.getInstance();
        userController = config.getUserController();
        budgetController = config.getBudgetController();
        goalController = config.getGoalController();
        administrationController = config.getAdministrationController();
        transactionController = config.getTransactionController();
        statisticController = config.getStatisticController();
    }

    public void run() {
        while (true) {
            System.out.println("""
                    1. Register
                    2. Login
                    
                    0. Exit
                    """);
            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();
            try {
                switch (choice) {
                    case 1 -> userController.register();
                    case 2 -> {
                        userController.login();
                        if (currentUser.getRole().equals(Role.USER)) {
                            menuUser();
                        } else {
                            menuAdmin();
                        }
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
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
                    case 1 -> userController.profileSettings();
                    case 2 -> transactionController.financialManagement();
                    case 3 -> budgetController.budgetManagement();
                    case 4 -> goalController.goalManagement();
                    case 5 -> statisticController.statisticsAndAnalytics();

                    case 0 -> {
                        currentUser = null;
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
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
                    case 1 -> administrationController.findAllUsers();
                    case 2 -> administrationController.findAllTransactionsOfUsers();
                    case 3 -> administrationController.deleteUser();
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
}

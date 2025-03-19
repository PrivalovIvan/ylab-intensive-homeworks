package com.ylab.homework_1.ui.controller;

import com.ylab.homework_1.usecase.dto.BudgetDTO;
import com.ylab.homework_1.usecase.service.BudgetService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Scanner;

import static com.ylab.homework_1.ui.ConsoleApp.currentUser;

@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;
    Scanner scanner = new Scanner(System.in);

    public void budgetManagement() {
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
}

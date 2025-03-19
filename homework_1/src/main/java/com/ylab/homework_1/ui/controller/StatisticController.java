package com.ylab.homework_1.ui.controller;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.usecase.service.StatisticsService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

import static com.ylab.homework_1.ui.ConsoleApp.currentUser;
import static com.ylab.homework_1.ui.controller.ClearConsoleUtil.clearConsole;

@RequiredArgsConstructor
public class StatisticController {
    private final StatisticsService statisticsService;
    private final Scanner scanner = new Scanner(System.in);

    public void statisticsAndAnalytics() {
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
}

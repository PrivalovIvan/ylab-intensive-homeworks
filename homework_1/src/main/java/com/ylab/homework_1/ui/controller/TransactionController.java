package com.ylab.homework_1.ui.controller;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.service.BudgetService;
import com.ylab.homework_1.usecase.service.GoalService;
import com.ylab.homework_1.usecase.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static com.ylab.homework_1.ui.ConsoleApp.currentUser;
import static com.ylab.homework_1.ui.controller.ClearConsoleUtil.clearConsole;

@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final GoalService goalService;
    private final BudgetService budgetService;
    private final Scanner scanner = new Scanner(System.in);

    public void financialManagement() {
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
}

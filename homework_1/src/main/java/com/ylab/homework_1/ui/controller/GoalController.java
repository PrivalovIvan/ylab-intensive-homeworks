package com.ylab.homework_1.ui.controller;

import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static com.ylab.homework_1.ui.ConsoleApp.currentUser;

@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    private final Scanner scanner = new Scanner(System.in);

    public void goalManagement() {
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
}

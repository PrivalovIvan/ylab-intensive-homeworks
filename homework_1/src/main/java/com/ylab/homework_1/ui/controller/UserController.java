package com.ylab.homework_1.ui.controller;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.usecase.service.UserService;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static com.ylab.homework_1.ui.ConsoleApp.currentUser;
import static com.ylab.homework_1.ui.controller.ClearConsoleUtil.clearConsole;

@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);

    public void profileSettings() {
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
                    case 1 -> changeName();
                    case 2 -> changeEmail();
                    case 3 -> changePassword();
                    case 4 -> deleteProfile();
                    case 5 -> viewProfile();
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

    public void register() {
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

    public void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            currentUser = userService.login(email, password);
        } catch (IllegalArgumentException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeName() throws SQLException {
        System.out.print("New name: ");
        String name = scanner.nextLine();
        currentUser = userService.updateUser(currentUser.getUuid(), name, null, null);
        System.out.println("Name update successfully");
    }

    private void changeEmail() throws SQLException {
        System.out.print("New email: ");
        String email = scanner.nextLine();
        currentUser = userService.updateUser(currentUser.getUuid(), null, email, null);
        System.out.println("Email update successfully");
    }

    private void changePassword() throws SQLException {
        System.out.print("New password: ");
        String password = scanner.nextLine();
        currentUser = userService.updateUser(currentUser.getUuid(), null, null, password);
        System.out.println("Password update successfully");
    }

    private void deleteProfile() throws SQLException {
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
        }
    }

    private void viewProfile() throws SQLException {
        System.out.println(userService.findByUuid(currentUser.getUuid()));
    }
}

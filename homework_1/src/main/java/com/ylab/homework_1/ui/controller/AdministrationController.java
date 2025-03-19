package com.ylab.homework_1.ui.controller;

import com.ylab.homework_1.usecase.service.AdministrationService;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

@RequiredArgsConstructor
public class AdministrationController {
    private final AdministrationService administrationService;
    private final Scanner scanner = new Scanner(System.in);

    public void findAllUsers() throws SQLException {
        System.out.println("Users: ");
        System.out.println(administrationService.findAllUsers());
    }

    public void findAllTransactionsOfUsers() throws SQLException {
        System.out.println("Email users: ");
        String email = scanner.nextLine();
        System.out.println("Transactions: ");
        System.out.println(administrationService.findAllTransactionsOfUsers(email));
    }

    public void deleteUser() throws SQLException {
        System.out.println("Email user: ");
        String email = scanner.nextLine();
        administrationService.deleteUser(email);
    }
}

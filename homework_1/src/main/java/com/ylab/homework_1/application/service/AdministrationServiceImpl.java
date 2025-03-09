package com.ylab.homework_1.application.service;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.domain.service.AdministrationService;
import com.ylab.homework_1.domain.service.TransactionService;
import com.ylab.homework_1.domain.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AdministrationServiceImpl implements AdministrationService {
    private final UserService userService;
    private final TransactionService transactionService;

    @Override
    public List<User> findAllUsers() {
        return userService.findAll().stream()
                .filter(u -> u.getRole() == Role.USER)
                .toList();
    }

    @Override
    public List<Transaction> findAllTransactionsOfUsers(String email) {
        return transactionService.findAllTransactionUser(email);
    }

    @Override
    public void deleteUser(String email) {
        userService.delete(email);
    }
}

package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.usecase.service.AdministrationService;
import com.ylab.homework_1.usecase.service.TransactionService;
import com.ylab.homework_1.usecase.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AdministrationServiceImpl implements AdministrationService {
    private final UserService userService;
    private final TransactionService transactionService;

    @Override
    public List<UserDTO> findAllUsers() {
        return userService.findAll().stream()
                .filter(u -> u.getRole() == Role.USER)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionsOfUsers(String email) {
        return transactionService.findAllTransactionUser(email);
    }

    @Override
    public void deleteUser(String email) {
        userService.delete(email);
    }
}
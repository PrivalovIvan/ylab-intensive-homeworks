package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.usecase.service.AdministrationService;
import com.ylab.homework_1.usecase.service.TransactionService;
import com.ylab.homework_1.usecase.service.UserService;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AdministrationServiceImpl implements AdministrationService {
    private final UserService userService;
    private final TransactionService transactionService;

    @Override
    public List<UserDTO> findAllUsers() throws SQLException {
        return userService.findAll().stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionsOfUsers(String email) throws SQLException {
        Optional<UserDTO> user = Optional.of(userService.findByEmail(email));
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }
        return transactionService.findAllTransactionUser(email);
    }

    @Override
    public void deleteUser(String email) throws SQLException {
        Optional<UserDTO> user = Optional.of(userService.findByEmail(email));
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }
        if (user.get().getRole() == Role.ADMIN) {
            throw new IllegalStateException("Cannot delete an admin user: " + email);
        }
        userService.delete(email);
    }
}
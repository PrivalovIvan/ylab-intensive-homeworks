package com.ylab.finance_tracker_spring.service;

import com.ylab.finance_tracker_spring.common.Role;
import com.ylab.finance_tracker_spring.domain.service.AdministrationService;
import com.ylab.finance_tracker_spring.domain.service.TransactionService;
import com.ylab.finance_tracker_spring.domain.service.UserService;
import com.ylab.finance_tracker_spring.dto.TransactionDTO;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
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
        return transactionService.findAllTransactionUser(email);
    }

    @Override
    public void deleteUser(String email) throws SQLException {
        Optional<UserDTO> user = Optional.of(userService.findByEmail(email));
        if (user.get().getRole() == Role.ADMIN) {
            throw new IllegalStateException("Cannot delete an admin user: " + email);
        }
        userService.delete(email);
    }
}
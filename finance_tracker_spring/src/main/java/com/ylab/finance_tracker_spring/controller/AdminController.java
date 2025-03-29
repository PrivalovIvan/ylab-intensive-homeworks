package com.ylab.finance_tracker_spring.controller;

import com.ylab.finance_tracker_spring.annotation.Loggable;
import com.ylab.finance_tracker_spring.domain.service.AdministrationService;
import com.ylab.finance_tracker_spring.dto.TransactionDTO;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
@Loggable
public class AdminController {
    private final AdministrationService administrationService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() throws SQLException {
        List<UserDTO> users = administrationService.findAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(@RequestParam String email) throws SQLException {
        List<TransactionDTO> transactions = administrationService.findAllTransactionsOfUsers(email);
        return transactions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String email) throws SQLException {
        try {
            administrationService.deleteUser(email);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}

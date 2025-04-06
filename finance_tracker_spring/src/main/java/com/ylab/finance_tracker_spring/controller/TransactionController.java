package com.ylab.finance_tracker_spring.controller;

import com.ylab.finance_tracker_spring.annotation.Loggable;
import com.ylab.finance_tracker_spring.common.TransactionType;
import com.ylab.finance_tracker_spring.domain.service.TransactionService;
import com.ylab.finance_tracker_spring.dto.TransactionDTO;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import com.ylab.finance_tracker_spring.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@Validated
@Loggable
public class TransactionController {
    private final TransactionService transactionService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> showAllTransactions() throws SQLException {
        var transactions = transactionService.findAllTransactionUser(authService.getCurrentUser().getEmail());
        return transactions != null ? ResponseEntity.ok(transactions) : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws SQLException {
        transactionDTO.setEmail(authService.getCurrentUser().getEmail());
        transactionDTO.setDate(LocalDate.now());
        transactionService.createTransaction(transactionDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter/date")
    public ResponseEntity<List<TransactionDTO>> filterTransactionDate(@RequestParam String date) throws SQLException {
        var transactions = transactionService.findAllTransactionFilterByDate(authService.getCurrentUser().getEmail(), LocalDate.parse(date));
        return transactions != null ? ResponseEntity.ok(transactions) : ResponseEntity.notFound().build();
    }

    @GetMapping("/filter/category")
    public ResponseEntity<List<TransactionDTO>> filterTransactionCategory(@RequestParam String category) throws SQLException {
        var transactions = transactionService.findAllTransactionFilterByCategory(authService.getCurrentUser().getEmail(), category);
        return transactions != null ? ResponseEntity.ok(transactions) : ResponseEntity.notFound().build();
    }

    @GetMapping("/filter/type")
    public ResponseEntity<List<TransactionDTO>> filterTransactionType(@RequestParam String type) throws SQLException {
        var transactions = transactionService.findAllTransactionFilterByType(authService.getCurrentUser().getEmail(), TransactionType.valueOf(type));
        return transactions != null ? ResponseEntity.ok(transactions) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTransaction(@RequestParam String id) throws SQLException {
        UserDTO currentUser = authService.getCurrentUser();
        transactionService.deleteTransaction(currentUser.getEmail(), UUID.fromString(id));
        return ResponseEntity.ok().build();
    }
}

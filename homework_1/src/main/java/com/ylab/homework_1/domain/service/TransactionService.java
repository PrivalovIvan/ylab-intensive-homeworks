package com.ylab.homework_1.domain.service;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.domain.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    void createTransaction(User currentUser, TransactionType transactionType, BigDecimal amount, String category, String description);

    void updateTransaction(User currentUser, UUID uuid, BigDecimal amount, String category, String description);

    void deleteTransaction(UUID uuid);

    List<Transaction> findAllTransactionUser(String email);

    List<Transaction> findAllTransactionFilterByDate(String email, LocalDate date);

    List<Transaction> findAllTransactionFilterByCategory(String email, String category);

    List<Transaction> findAllTransactionFilterByType(String email, TransactionType type);

}

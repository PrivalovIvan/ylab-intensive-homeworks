package com.ylab.homework_1.domain.repository;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository {
    void create(Transaction transaction);

    List<Transaction> findAll(Transaction transaction);

    List<Transaction> findByUserEmail(String email);

    void update(UUID uuid, BigDecimal amount, String category, String description);

    void delete(UUID uuid);

    List<Transaction> findByUserEmailFilterDate(String email, LocalDate date);

    List<Transaction> findByUserEmailFilterCategory(String email, String category);

    List<Transaction> findByUserEmailFilterType(String email, TransactionType type);

    List<Transaction> getTransactionsList();

    List<Transaction> findByUUID(UUID uuid);

}

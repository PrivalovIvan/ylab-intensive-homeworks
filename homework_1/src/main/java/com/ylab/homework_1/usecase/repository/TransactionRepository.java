package com.ylab.homework_1.usecase.repository;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void save(Transaction transaction);

    List<Transaction> getTransactionsByUserEmail(String email);

    Optional<Transaction> getByUUID(UUID uuid);

    void delete(String email, UUID uuid);

    List<Transaction> getTransactionsByUserEmailFilterDate(String email, LocalDate date);

    List<Transaction> getTransactionsByUserEmailFilterCategory(String email, String category);

    List<Transaction> getTransactionsByUserEmailFilterType(String email, TransactionType type);
}

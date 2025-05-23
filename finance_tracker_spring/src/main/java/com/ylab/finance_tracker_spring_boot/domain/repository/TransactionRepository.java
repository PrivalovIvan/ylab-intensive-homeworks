package com.ylab.finance_tracker_spring_boot.domain.repository;

import com.ylab.finance_tracker_spring_boot.common.TransactionType;
import com.ylab.finance_tracker_spring_boot.domain.model.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void save(Transaction transaction) throws SQLException;

    void update(Transaction transaction) throws SQLException;

    List<Transaction> getTransactionsByUserEmail(String email) throws SQLException;

    Optional<Transaction> getById(UUID id) throws SQLException;

    void delete(String email, UUID id) throws SQLException;

    List<Transaction> getTransactionsByUserEmailFilterDate(String email, LocalDate date) throws SQLException;

    List<Transaction> getTransactionsByUserEmailFilterCategory(String email, String category) throws SQLException;

    List<Transaction> getTransactionsByUserEmailFilterType(String email, TransactionType type) throws SQLException;
}

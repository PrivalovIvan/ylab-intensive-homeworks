package com.ylab.homework_1.usecase.repository;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction) throws SQLException;

    void update(Transaction transaction) throws SQLException;

    List<Transaction> getTransactionsByUserEmail(String email) throws SQLException;

    Optional<Transaction> getById(Long id) throws SQLException;

    void delete(String email, Long id) throws SQLException;

    List<Transaction> getTransactionsByUserEmailFilterDate(String email, LocalDate date) throws SQLException;

    List<Transaction> getTransactionsByUserEmailFilterCategory(String email, String category) throws SQLException;

    List<Transaction> getTransactionsByUserEmailFilterType(String email, TransactionType type) throws SQLException;
}

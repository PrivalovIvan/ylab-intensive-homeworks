package com.ylab.finance_tracker.usecase.service;

import com.ylab.finance_tracker.common.TransactionType;
import com.ylab.finance_tracker.usecase.dto.TransactionDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    void createTransaction(TransactionDTO transactionDTO) throws SQLException;

    void updateAmount(String email, UUID id, BigDecimal amount) throws SQLException;

    void updateCategory(String email, UUID id, String category) throws SQLException;

    void updateDescription(String email, UUID id, String description) throws SQLException;

    void deleteTransaction(String email, UUID id) throws SQLException;

    List<TransactionDTO> findAllTransactionUser(String email) throws SQLException;

    List<TransactionDTO> findAllTransactionFilterByDate(String email, LocalDate date) throws SQLException;

    List<TransactionDTO> findAllTransactionFilterByCategory(String email, String category) throws SQLException;

    List<TransactionDTO> findAllTransactionFilterByType(String email, TransactionType type) throws SQLException;

}

package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.usecase.dto.TransactionDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    void createTransaction(TransactionDTO transactionDTO) throws SQLException;

    void updateAmount(String email, Long id, BigDecimal amount) throws SQLException;

    void updateCategory(String email, Long id, String category) throws SQLException;

    void updateDescription(String email, Long id, String description) throws SQLException;

    void deleteTransaction(String email, Long id) throws SQLException;

    List<TransactionDTO> findAllTransactionUser(String email) throws SQLException;

    List<TransactionDTO> findAllTransactionFilterByDate(String email, LocalDate date) throws SQLException;

    List<TransactionDTO> findAllTransactionFilterByCategory(String email, String category) throws SQLException;

    List<TransactionDTO> findAllTransactionFilterByType(String email, TransactionType type) throws SQLException;

}

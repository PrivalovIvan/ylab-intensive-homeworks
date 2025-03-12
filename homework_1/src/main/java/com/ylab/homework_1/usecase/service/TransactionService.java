package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.usecase.dto.TransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    void createTransaction(TransactionDTO transactionDTO);

    void updateAmount(String email, UUID uuid, BigDecimal amount);

    void updateCategory(String email, UUID uuid, String category);

    void updateDescription(String email, UUID uuid, String description);

    void deleteTransaction(String email, UUID uuid);

    List<TransactionDTO> findAllTransactionUser(String email);

    List<TransactionDTO> findAllTransactionFilterByDate(String email, LocalDate date);

    List<TransactionDTO> findAllTransactionFilterByCategory(String email, String category);

    List<TransactionDTO> findAllTransactionFilterByType(String email, TransactionType type);

}

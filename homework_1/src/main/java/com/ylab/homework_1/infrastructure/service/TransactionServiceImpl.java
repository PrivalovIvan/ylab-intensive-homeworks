package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.infrastructure.mapper.TransactionMapper;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.repository.TransactionRepository;
import com.ylab.homework_1.usecase.service.TransactionService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(TransactionDTO transactionDTO) throws SQLException {
        transactionRepository.save(TransactionMapper.toTransaction.apply(transactionDTO));
    }

    @Override
    public void updateAmount(String email, UUID id, BigDecimal amount) throws SQLException {
        Transaction transaction = getTransactionOrThrow(email, id);
        transaction.setAmount(amount);
        transactionRepository.update(transaction);
    }

    @Override
    public void updateCategory(String email, UUID id, String category) throws SQLException {
        Transaction transaction = getTransactionOrThrow(email, id);
        transaction.setCategory(category);
        transactionRepository.update(transaction);
    }

    @Override
    public void updateDescription(String email, UUID id, String description) throws SQLException {
        Transaction transaction = getTransactionOrThrow(email, id);
        transaction.setDescription(description);
        transactionRepository.update(transaction);
    }

    @Override
    public void deleteTransaction(String email, UUID id) throws SQLException {
        transactionRepository.delete(email, id);
    }

    @Override
    public List<TransactionDTO> findAllTransactionUser(String email) throws SQLException {
        return transactionRepository.getTransactionsByUserEmail(email).stream()
                .map(TransactionMapper.toTransactionDTO::apply)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByDate(String email, LocalDate date) throws SQLException {
        return transactionRepository.getTransactionsByUserEmailFilterDate(email, date).stream()
                .map(TransactionMapper.toTransactionDTO::apply)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByCategory(String email, String category) throws SQLException {
        return transactionRepository.getTransactionsByUserEmailFilterCategory(email, category).stream()
                .map(TransactionMapper.toTransactionDTO::apply)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByType(String email, TransactionType type) throws SQLException {
        return transactionRepository.getTransactionsByUserEmailFilterType(email, type).stream()
                .map(TransactionMapper.toTransactionDTO::apply)
                .toList();
    }

    private Transaction getTransactionOrThrow(String email, UUID id) throws SQLException {
        return transactionRepository.getById(id)
                .filter(t -> t.getEmail().equals(email))
                .orElseThrow(() -> new IllegalArgumentException("Transaction with id " + id + " not found for email " + email));
    }
}
package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.mapper.TransactionMapper;
import com.ylab.homework_1.usecase.repository.TransactionRepository;
import com.ylab.homework_1.usecase.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;


    @Override
    public void createTransaction(TransactionDTO transactionDTO) {
        transactionRepository.save(transactionMapper.toTransaction(transactionDTO));
    }

    @Override
    public void updateAmount(String email, UUID uuid, BigDecimal amount) {
        transactionRepository.getByUUID(uuid)
                .filter(transaction -> transaction.getEmail().equals(email))
                .ifPresent(transaction -> transaction.setAmount(amount));
    }

    @Override
    public void updateCategory(String email, UUID uuid, String category) {
        transactionRepository.getByUUID(uuid)
                .filter(transaction -> transaction.getEmail().equals(email))
                .ifPresent(transaction -> transaction.setCategory(category));
    }

    @Override
    public void updateDescription(String email, UUID uuid, String description) {
        transactionRepository.getByUUID(uuid)
                .filter(transaction -> transaction.getEmail().equals(email))
                .ifPresent(transaction -> transaction.setDescription(description));
    }

    @Override
    public void deleteTransaction(String email, UUID uuid) {
        transactionRepository.delete(email, uuid);
    }

    @Override
    public List<TransactionDTO> findAllTransactionUser(String email) {
        return transactionRepository.getTransactionsByUserEmail(email).stream().map(transactionMapper::toTransactionDTO).toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByDate(String email, LocalDate date) {
        return transactionRepository.getTransactionsByUserEmailFilterDate(email, date).stream().map(transactionMapper::toTransactionDTO).toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByCategory(String email, String category) {
        return transactionRepository.getTransactionsByUserEmailFilterCategory(email, category).stream().map(transactionMapper::toTransactionDTO).toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByType(String email, TransactionType type) {
        return transactionRepository.getTransactionsByUserEmailFilterType(email, type).stream().map(transactionMapper::toTransactionDTO).toList();
    }
}

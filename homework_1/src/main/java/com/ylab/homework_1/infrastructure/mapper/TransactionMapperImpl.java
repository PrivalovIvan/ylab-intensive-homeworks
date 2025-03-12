package com.ylab.homework_1.infrastructure.mapper;

import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.mapper.TransactionMapper;

public class TransactionMapperImpl implements TransactionMapper {
    @Override
    public Transaction toTransaction(TransactionDTO transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        return new Transaction(
                transaction.getUuid(),
                transaction.getEmail(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getNameGoal(),
                transaction.getDate(),
                transaction.getDescription());
    }

    @Override
    public TransactionDTO toTransactionDTO(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        return new TransactionDTO(
                transaction.getUuid(),
                transaction.getEmail(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getNameGoal(),
                transaction.getDate(),
                transaction.getDescription());
    }
}

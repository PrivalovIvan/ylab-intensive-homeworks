package com.ylab.homework_1.infrastructure.mapper;

import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.mapper.Mapper;

public class TransactionMapper {
    public static final Mapper<Transaction, TransactionDTO> toTransaction = transactionDTO ->
            new Transaction(
                    transactionDTO.getUuid(),
                    transactionDTO.getEmail(),
                    transactionDTO.getType(),
                    transactionDTO.getAmount(),
                    transactionDTO.getCategory(),
                    transactionDTO.getNameGoal(),
                    transactionDTO.getDate(),
                    transactionDTO.getDescription());

    public static final Mapper<TransactionDTO, Transaction> toTransactionDTO = transaction ->
            new TransactionDTO(
                    transaction.getUuid(),
                    transaction.getEmail(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getCategory(),
                    transaction.getNameGoal(),
                    transaction.getDate(),
                    transaction.getDescription());
}

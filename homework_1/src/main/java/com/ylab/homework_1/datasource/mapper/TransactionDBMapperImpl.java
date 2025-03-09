package com.ylab.homework_1.datasource.mapper;

import com.ylab.homework_1.datasource.model.TransactionDB;
import com.ylab.homework_1.domain.model.Transaction;

import java.util.Optional;

public class TransactionDBMapperImpl implements TransactionDBMapper {
    @Override
    public Optional<Transaction> toTransaction(TransactionDB transactionDB) {
        return Optional.ofNullable(transactionDB)
                .map(transaction -> new Transaction(
                        transaction.getEmail(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getCategory(),
                        transaction.getDescription()
                ));
    }

    @Override
    public Optional<TransactionDB> toTransactionDB(Transaction transaction) {
        return Optional.ofNullable(transaction)
                .map(transactionDB -> new TransactionDB(
                        transactionDB.getUuid(),
                        transactionDB.getEmail(),
                        transactionDB.getType(),
                        transactionDB.getAmount(),
                        transactionDB.getCategory(),
                        transactionDB.getDate(),
                        transactionDB.getDescription()
                ));
    }
}

package com.ylab.homework_1.datasource.mapper;

import com.ylab.homework_1.datasource.model.TransactionDB;
import com.ylab.homework_1.domain.model.Transaction;

import java.util.Optional;

public interface TransactionDBMapper {
    Optional<Transaction> toTransaction(TransactionDB transactionDB);

    Optional<TransactionDB> toTransactionDB(Transaction transaction);
}

package com.ylab.homework_1.usecase.mapper;

import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.usecase.dto.TransactionDTO;

public interface TransactionMapper {
    Transaction toTransaction(TransactionDTO transaction);

    TransactionDTO toTransactionDTO(Transaction transaction);
}

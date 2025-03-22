package com.ylab.finance_tracker.usecase.mapper;

import com.ylab.finance_tracker.domain.model.Transaction;
import com.ylab.finance_tracker.usecase.dto.TransactionDTO;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {
    TransactionDTO toTransactionDTO(Transaction transaction);

    Transaction toTransaction(TransactionDTO transactionDTO);
}

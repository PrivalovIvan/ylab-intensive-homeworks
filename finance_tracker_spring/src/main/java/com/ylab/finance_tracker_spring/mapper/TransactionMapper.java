package com.ylab.finance_tracker_spring.mapper;

import com.ylab.finance_tracker_spring.domain.model.Transaction;
import com.ylab.finance_tracker_spring.dto.TransactionDTO;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {
    TransactionDTO toTransactionDTO(Transaction transaction);

    Transaction toTransaction(TransactionDTO transactionDTO);
}

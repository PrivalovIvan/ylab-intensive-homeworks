package com.ylab.finance_tracker_spring_boot.mapper;

import com.ylab.finance_tracker_spring_boot.domain.model.Transaction;
import com.ylab.finance_tracker_spring_boot.dto.TransactionDTO;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {
    TransactionDTO toTransactionDTO(Transaction transaction);

    Transaction toTransaction(TransactionDTO transactionDTO);
}

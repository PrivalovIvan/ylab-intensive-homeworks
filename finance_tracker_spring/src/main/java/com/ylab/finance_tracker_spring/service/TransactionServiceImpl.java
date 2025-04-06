package com.ylab.finance_tracker_spring.service;

import com.ylab.finance_tracker_spring.common.TransactionType;
import com.ylab.finance_tracker_spring.domain.model.Transaction;
import com.ylab.finance_tracker_spring.domain.repository.TransactionRepository;
import com.ylab.finance_tracker_spring.domain.service.TransactionService;
import com.ylab.finance_tracker_spring.dto.TransactionDTO;
import com.ylab.finance_tracker_spring.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);


    @Override
    public void createTransaction(TransactionDTO transactionDTO) throws SQLException {
        transactionRepository.save(transactionMapper.toTransaction(transactionDTO));
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
                .map(transactionMapper::toTransactionDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByDate(String email, LocalDate date) throws SQLException {
        return transactionRepository.getTransactionsByUserEmailFilterDate(email, date).stream()
                .map(transactionMapper::toTransactionDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByCategory(String email, String category) throws SQLException {
        return transactionRepository.getTransactionsByUserEmailFilterCategory(email, category).stream()
                .map(transactionMapper::toTransactionDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionFilterByType(String email, TransactionType type) throws SQLException {
        return transactionRepository.getTransactionsByUserEmailFilterType(email, type).stream()
                .map(transactionMapper::toTransactionDTO)
                .toList();
    }

    private Transaction getTransactionOrThrow(String email, UUID id) throws SQLException {
        return transactionRepository.getById(id)
                .filter(t -> t.getEmail().equals(email))
                .orElseThrow(() -> new IllegalArgumentException("Transaction with id " + id + " not found for email " + email));
    }
}
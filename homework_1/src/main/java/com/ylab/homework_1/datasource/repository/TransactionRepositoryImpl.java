package com.ylab.homework_1.datasource.repository;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.datasource.mapper.TransactionDBMapper;
import com.ylab.homework_1.datasource.model.TransactionDB;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.domain.repository.TransactionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final Map<UUID, TransactionDB> transactions = new HashMap<>();
    private final TransactionDBMapper transactionDBMapper;

    @Override
    public List<Transaction> getTransactionsList() {
        return transactions.values().stream()
                .map(transactionDBMapper::toTransaction)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void create(Transaction transaction) throws IllegalArgumentException {
        transactionDBMapper.toTransactionDB(transaction).ifPresent(t -> transactions.put(t.getUuid(), t));
    }

    @Override
    public List<Transaction> findAll(Transaction transaction) {
        if (transaction == null) throw new IllegalArgumentException("transaction cannot be null");
        return transactions.values().stream()
                .map(transactionDBMapper::toTransaction)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public List<Transaction> findByUserEmail(String email) {
        return transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .map(transactionDBMapper::toTransaction)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public List<Transaction> findByUUID(UUID uuid) {
        return transactions.values().stream()
                .filter(t -> t.getUuid().equals(uuid))
                .map(transactionDBMapper::toTransaction)
                .flatMap(Optional::stream)
                .toList();
    }


    @Override
    public void update(UUID uuid, BigDecimal amount, String category, String description) {
        TransactionDB transactionDB = transactions.get(uuid);

        if (transactionDB != null) {
            transactionDB.setAmount(amount);
            transactionDB.setCategory(category);
            transactionDB.setDescription(description);
            transactions.put(uuid, transactionDB);
        }
    }

    @Override
    public void delete(UUID uuid) {
        transactions.remove(uuid);
    }

    public List<Transaction> findByUserEmailFilterDate(String email, LocalDate date) {
        return transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .filter(t -> t.getDate().equals(date))
                .map(transactionDBMapper::toTransaction)
                .flatMap(Optional::stream)
                .toList();
    }

    public List<Transaction> findByUserEmailFilterCategory(String email, String category) {
        return transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .filter(t -> t.getCategory().equals(category))
                .map(transactionDBMapper::toTransaction)
                .flatMap(Optional::stream)
                .toList();
    }

    public List<Transaction> findByUserEmailFilterType(String email, TransactionType type) {
        return transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .filter(t -> t.getType().equals(type))
                .map(transactionDBMapper::toTransaction)
                .flatMap(Optional::stream)
                .toList();
    }

}


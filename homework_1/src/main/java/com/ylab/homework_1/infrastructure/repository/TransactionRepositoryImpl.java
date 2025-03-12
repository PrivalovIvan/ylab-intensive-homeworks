package com.ylab.homework_1.infrastructure.repository;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.usecase.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.*;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Map<UUID, Transaction> transactions = new HashMap<>();

    @Override
    public void save(Transaction transaction) {
        transactions.put(transaction.getUuid(), transaction);
    }

    @Override
    public List<Transaction> getTransactionsByUserEmail(String email) {
        return transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .toList();
    }

    @Override
    public Optional<Transaction> getByUUID(UUID uuid) {
        return transactions.values().stream()
                .filter(t -> t.getUuid().equals(uuid))
                .findFirst();
    }

    @Override
    public void delete(String email, UUID uuid) {
        transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .findFirst()
                .ifPresent(t -> transactions.remove(uuid));
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterDate(String email, LocalDate date) {
        return transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .filter(t -> t.getDate().equals(date))
                .toList();
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterCategory(String email, String category) {
        return transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .filter(t -> t.getCategory().equals(category))
                .toList();
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterType(String email, TransactionType type) {
        return transactions.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .filter(t -> t.getType().equals(type))
                .toList();
    }
}

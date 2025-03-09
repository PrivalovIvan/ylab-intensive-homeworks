package com.ylab.homework_1.application.service;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.domain.repository.BudgetRepository;
import com.ylab.homework_1.domain.repository.TransactionRepository;
import com.ylab.homework_1.domain.service.BudgetService;
import com.ylab.homework_1.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final BudgetService budgetService;

    @Override
    public void createTransaction(User currentUser, TransactionType transactionType, BigDecimal amount, String category, String description) {
        Transaction transaction = new Transaction(
                currentUser.getEmail(),
                transactionType,
                amount,
                category,
                description
        );
        transactionRepository.create(transaction);


        if (transactionType == TransactionType.EXPENSE) {
            budgetRepository.findByUserAndMonth(transaction.getEmail(), YearMonth.from(transaction.getDate()))
                    .ifPresentOrElse(budget -> {
                                budget.setSpent(budget.getSpent().add(amount));
                                budgetRepository.save(budget);
                                budgetService.checkAndNotify(currentUser.getEmail(),
                                        YearMonth.from(transaction.getDate()));
                            },
                            () -> {
                                Budget newBudget = new Budget(transaction.getEmail(),
                                        YearMonth.from(transaction.getDate()),
                                        BigDecimal.ZERO, transaction.getAmount());
                                budgetRepository.save(newBudget);
                            });
        }
    }

    @Override
    public void updateTransaction(User currentUser, UUID uuid, BigDecimal amount, String category, String description) {
        transactionRepository.update(
                uuid,
                amount,
                category,
                description
        );
    }

    @Override
    public void deleteTransaction(UUID uuid) {
        transactionRepository.delete(uuid);
    }

    @Override
    public List<Transaction> findAllTransactionUser(String email) {
        return transactionRepository.findByUserEmail(email);
    }

    public List<Transaction> findAllTransactionFilterByDate(String email, LocalDate date) {
        return transactionRepository.findByUserEmailFilterDate(email, date);
    }

    public List<Transaction> findAllTransactionFilterByCategory(String email, String category) {
        return transactionRepository.findByUserEmailFilterCategory(email, category);
    }

    public List<Transaction> findAllTransactionFilterByType(String email, TransactionType type) {
        return transactionRepository.findByUserEmailFilterType(email, type);
    }
}

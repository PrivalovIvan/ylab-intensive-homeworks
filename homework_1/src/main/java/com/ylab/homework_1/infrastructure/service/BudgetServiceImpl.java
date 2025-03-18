package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.infrastructure.mapper.BudgetMapper;
import com.ylab.homework_1.usecase.dto.BudgetDTO;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.repository.BudgetRepository;
import com.ylab.homework_1.usecase.service.BudgetService;
import com.ylab.homework_1.usecase.service.NotificationService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Optional;

public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final NotificationService notificationService;

    public BudgetServiceImpl(BudgetRepository budgetRepository, NotificationService notificationService) {
        this.budgetRepository = budgetRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void createBudget(BudgetDTO budgetDTO) throws SQLException {
        budgetRepository.save(BudgetMapper.toBudget.apply(budgetDTO));
    }

    @Override
    public Optional<Budget> getBudget(String email, YearMonth month) throws SQLException {
        return budgetRepository.findByUserAndMonth(email, month);
    }

    @Override
    public void addExpense(String email, YearMonth month, BigDecimal amount) throws SQLException {
        Optional<Budget> budgetOpt = budgetRepository.findByUserAndMonth(email, month);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            BigDecimal newSpent = budget.getSpent().add(amount);
            Budget updatedBudget = Budget.builder()
                    .email(email)
                    .yearMonth(month)
                    .budget(budget.getBudget())
                    .spent(newSpent)
                    .build();
            budgetRepository.update(updatedBudget);
            checkAndNotify(email, month);
        } else {
            throw new IllegalStateException("Budget not found for " + email + " and " + month);
        }
    }

    @Override
    public boolean isBudgetExceeded(String email, YearMonth month) throws SQLException {
        return budgetRepository.findByUserAndMonth(email, month)
                .map(Budget::isExceeded)
                .orElse(false);
    }

    @Override
    public void checkAndNotify(String email, YearMonth month) throws SQLException {
        budgetRepository.findByUserAndMonth(email, month)
                .filter(Budget::isExceeded)
                .ifPresent(budget -> notificationService.send(email, "Вы превысили бюджет на: " +
                        budget.getSpent().subtract(budget.getBudget())));
    }

    @Override
    public void processExpense(TransactionDTO transactionDTO) throws SQLException {
        YearMonth yearMonth = YearMonth.from(transactionDTO.getDate());
        addExpense(transactionDTO.getEmail(), yearMonth, transactionDTO.getAmount());
    }
}
package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.infrastructure.mapper.BudgetMapper;
import com.ylab.homework_1.usecase.dto.BudgetDTO;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.repository.BudgetRepository;
import com.ylab.homework_1.usecase.service.BudgetService;
import com.ylab.homework_1.usecase.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final NotificationService notificationService;

    @Override
    public void createBudget(BudgetDTO budgetDTO) {
        budgetRepository.save(BudgetMapper.toBudget.apply(budgetDTO));
    }

    @Override
    public Optional<Budget> getBudget(String email, YearMonth month) {
        return budgetRepository.findByUserAndMonth(email, month);
    }

    @Override
    public void addExpense(String email, YearMonth month, BigDecimal amount) {
        budgetRepository.findByUserAndMonth(email, month).ifPresentOrElse(budget -> {
            budget.addExpense(amount);
            budgetRepository.save(budget);
            checkAndNotify(email, month);
        }, () -> new IllegalStateException("No budget found"));
    }

    @Override
    public boolean isBudgetExceeded(String email, YearMonth month) {
        return budgetRepository.findByUserAndMonth(email, month)
                .map(Budget::isExceeded)
                .orElse(false);
    }

    @Override
    public void checkAndNotify(String email, YearMonth month) {
        budgetRepository.findByUserAndMonth(email, month)
                .filter(Budget::isExceeded)
                .ifPresent(budget -> {
                    notificationService.send(email, "Вы превысили бюджет на: "
                            + budget.getSpent().subtract(budget.getBudget()));
                });
    }

    @Override
    public void processExpense(TransactionDTO transactionDTO) {
        YearMonth yearMonth = YearMonth.from(transactionDTO.getDate());
        budgetRepository.findByUserAndMonth(transactionDTO.getEmail(), yearMonth)
                .ifPresent(budget -> addExpense(transactionDTO.getEmail(), yearMonth, transactionDTO.getAmount()));
    }
}

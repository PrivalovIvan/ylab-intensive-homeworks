package com.ylab.homework_1.application.service;

import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.domain.repository.BudgetRepository;
import com.ylab.homework_1.domain.service.BudgetService;
import com.ylab.homework_1.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final NotificationService notificationService;

    @Override
    public void createBudget(String email, YearMonth month, BigDecimal limit) {
        Budget budget = new Budget(email, month, limit);
        budgetRepository.save(budget);
    }

    @Override
    public Optional<Budget> getBudget(String email, YearMonth month) {
        return budgetRepository.findByUserAndMonth(email, month);
    }

    @Override
    public void addExpense(String email, YearMonth month, BigDecimal amount) {
        Optional<Budget> budgetOpt = budgetRepository.findByUserAndMonth(email, month);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            budget.addExpense(amount);
            budgetRepository.save(budget);
//            if (budget.getSpent().compareTo(budget.getBudget()) > 0) {
//                System.out.println("budget exceeded!");
//            }
            checkAndNotify(email, month);
        }
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
                .ifPresent(budget -> notificationService.send(email, "Вы превысили бюджет за "
                        + month.getMonth() + " месяц, на: "
                        + budget.getSpent().subtract(budget.getBudget())));
    }
}
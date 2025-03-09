package com.ylab.homework_1.domain.service;

import com.ylab.homework_1.domain.model.Budget;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

public interface BudgetService {
    void createBudget(String email, YearMonth month, BigDecimal limit);

    Optional<Budget> getBudget(String email, YearMonth month);

    void addExpense(String email, YearMonth month, BigDecimal amount);

    boolean isBudgetExceeded(String email, YearMonth month);

    void checkAndNotify(String email, YearMonth month);

}

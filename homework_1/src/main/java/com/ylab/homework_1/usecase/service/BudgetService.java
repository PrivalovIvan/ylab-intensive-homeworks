package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.usecase.dto.BudgetDTO;
import com.ylab.homework_1.usecase.dto.TransactionDTO;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

public interface BudgetService {
    void createBudget(BudgetDTO budgetDTO);

    Optional<Budget> getBudget(String email, YearMonth month);

    void addExpense(String email, YearMonth month, BigDecimal amount);

    boolean isBudgetExceeded(String email, YearMonth month);

    void checkAndNotify(String email, YearMonth month);

    void processExpense(TransactionDTO transactionDTO);
}

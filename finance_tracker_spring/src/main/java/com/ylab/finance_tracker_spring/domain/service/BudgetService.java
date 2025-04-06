package com.ylab.finance_tracker_spring.domain.service;

import com.ylab.finance_tracker_spring.dto.BudgetDTO;
import com.ylab.finance_tracker_spring.dto.TransactionDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Optional;

public interface BudgetService {
    void createBudget(BudgetDTO budgetDTO) throws SQLException;

    Optional<BudgetDTO> getBudget(String email, YearMonth month) throws SQLException;

    void addExpense(String email, YearMonth month, BigDecimal amount) throws SQLException;

    boolean isBudgetExceeded(String email, YearMonth month) throws SQLException;

    void checkAndNotify(String email, YearMonth month) throws SQLException;

    void processExpense(TransactionDTO transactionDTO) throws SQLException;
}

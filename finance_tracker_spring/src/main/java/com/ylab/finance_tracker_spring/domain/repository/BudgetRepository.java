package com.ylab.finance_tracker_spring.domain.repository;

import com.ylab.finance_tracker_spring.domain.model.Budget;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Optional;

public interface BudgetRepository {
    void save(Budget budget) throws SQLException;

    void update(Budget budget) throws SQLException;

    Optional<Budget> findByUserAndMonth(String email, YearMonth month) throws SQLException;

    void delete(String email, YearMonth month) throws SQLException;
}

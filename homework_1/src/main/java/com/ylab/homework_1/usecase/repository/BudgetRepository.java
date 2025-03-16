package com.ylab.homework_1.usecase.repository;

import com.ylab.homework_1.domain.model.Budget;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Optional;

public interface BudgetRepository {
    void save(Budget budget) throws SQLException;

    void update(Budget budget) throws SQLException;

    Optional<Budget> findByUserAndMonth(String email, YearMonth month) throws SQLException;

    void delete(String email, YearMonth month) throws SQLException;
}

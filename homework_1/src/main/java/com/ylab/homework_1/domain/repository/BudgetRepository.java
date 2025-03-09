package com.ylab.homework_1.domain.repository;

import com.ylab.homework_1.domain.model.Budget;

import java.time.YearMonth;
import java.util.Optional;

public interface BudgetRepository {
    void save(Budget budget);
    Optional<Budget> findByUserAndMonth(String email, YearMonth month);
    void delete(String email, YearMonth month);
}

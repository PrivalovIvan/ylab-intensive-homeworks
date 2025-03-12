package com.ylab.homework_1.infrastructure.repository;

import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.usecase.repository.BudgetRepository;

import java.time.YearMonth;
import java.util.*;

public class BudgetRepositoryImpl implements BudgetRepository {
    private final List<Budget> budgets = new ArrayList<>();

    @Override
    public void save(Budget budget) {
        budgets.add(budget);
    }

    @Override
    public Optional<Budget> findByUserAndMonth(String email, YearMonth month) {
        return budgets.stream().
                filter(b -> b.getEmail().equals(email) && b.getYearMonth().equals(month))
                .findFirst();
    }

    @Override
    public void delete(String email, YearMonth month) {
        budgets.removeIf(b -> b.getEmail().equals(email) && b.getYearMonth().equals(month));
    }
}

package com.ylab.homework_1.datasource.repository;

import com.ylab.homework_1.datasource.mapper.BudgetDBMapper;
import com.ylab.homework_1.datasource.model.BudgetDB;
import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.domain.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetRepository {
    private final BudgetDBMapper budgetDBMapper;
    private final Map<String, Map<YearMonth, BudgetDB>> budgets = new HashMap<>();

    @Override
    public void save(Budget budget) {
        budgetDBMapper.toBudgetDB(budget).ifPresent(budgetDB -> {
            budgets.computeIfAbsent(budget.getEmail(), k -> new HashMap<>()).put(budget.getYearMonth(), budgetDB);
        });
    }

    @Override
    public Optional<Budget> findByUserAndMonth(String email, YearMonth month) {
        return Optional.ofNullable(budgets.getOrDefault(email, new HashMap<>()).get(month)).flatMap(budgetDBMapper::toBudget);
    }

    @Override
    public void delete(String email, YearMonth month) {
        Map<YearMonth, BudgetDB> userBudgets = budgets.get(email);
        if (userBudgets != null) {
            userBudgets.remove(month);
            if (userBudgets.isEmpty()) {
                budgets.remove(email);
            }
        }
    }
}
package com.ylab.homework_1.datasource.mapper;

import com.ylab.homework_1.datasource.model.BudgetDB;
import com.ylab.homework_1.domain.model.Budget;

import java.util.Optional;

public class BudgetDBMapperImpl implements BudgetDBMapper {
    @Override
    public Optional<Budget> toBudget(BudgetDB budgetDB) {
        return Optional.ofNullable(budgetDB)
                .map(budget -> new Budget(
                        budget.getEmail(),
                        budget.getYearMonth(),
                        budget.getBudget(),
                        budget.getSpent()
                ));
    }

    @Override
    public Optional<BudgetDB> toBudgetDB(Budget budget) {
        return Optional.ofNullable(budget)
                .map(budgetDB -> new BudgetDB(
                        budgetDB.getEmail(),
                        budgetDB.getYearMonth(),
                        budgetDB.getBudget(),
                        budgetDB.getSpent()
                ));
    }
}

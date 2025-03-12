package com.ylab.homework_1.infrastructure.mapper;

import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.usecase.dto.BudgetDTO;
import com.ylab.homework_1.usecase.mapper.BudgetMapper;

public class BudgetMapperImpl implements BudgetMapper {
    @Override
    public Budget toBudget(BudgetDTO budget) {
        if (budget == null) {
            throw new IllegalArgumentException("budget is null");
        }
        return new Budget(budget.getEmail(), budget.getYearMonth(), budget.getBudget(), budget.getSpent());
    }

    @Override
    public BudgetDTO toBudgetDTO(Budget budget) {
        if (budget == null) {
            throw new IllegalArgumentException("budget is null");
        }
        return new BudgetDTO(budget.getEmail(), budget.getYearMonth(), budget.getBudget(), budget.getSpent());
    }
}

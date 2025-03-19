package com.ylab.homework_1.infrastructure.mapper;

import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.usecase.dto.BudgetDTO;
import com.ylab.homework_1.usecase.mapper.Mapper;

public class BudgetMapper {
    public static final Mapper<Budget, BudgetDTO> toBudget = (budgetDTO) ->
            new Budget(budgetDTO.getUuid(), budgetDTO.getEmail(), budgetDTO.getYearMonth(), budgetDTO.getBudget(), budgetDTO.getSpent());

    public static final Mapper<BudgetDTO, Budget> toBudgetDTO = (budget) ->
            new BudgetDTO(budget.getUuid(), budget.getEmail(), budget.getYearMonth(), budget.getBudget(), budget.getSpent());
}

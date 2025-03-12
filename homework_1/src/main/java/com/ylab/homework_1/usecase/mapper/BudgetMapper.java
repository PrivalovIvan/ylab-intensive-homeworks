package com.ylab.homework_1.usecase.mapper;

import com.ylab.homework_1.domain.model.Budget;
import com.ylab.homework_1.usecase.dto.BudgetDTO;

public interface BudgetMapper {
    Budget toBudget(BudgetDTO budget);

    BudgetDTO toBudgetDTO(Budget budget);
}

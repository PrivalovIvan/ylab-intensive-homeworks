package com.ylab.finance_tracker_spring.mapper;

import com.ylab.finance_tracker_spring.domain.model.Budget;
import com.ylab.finance_tracker_spring.dto.BudgetDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BudgetMapper {
    BudgetDTO toBudgetDTO(Budget budget);

    Budget toBudget(BudgetDTO budgetDTO);
}

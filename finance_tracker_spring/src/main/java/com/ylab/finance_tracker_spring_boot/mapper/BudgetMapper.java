package com.ylab.finance_tracker_spring_boot.mapper;

import com.ylab.finance_tracker_spring_boot.domain.model.Budget;
import com.ylab.finance_tracker_spring_boot.dto.BudgetDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BudgetMapper {
    BudgetDTO toBudgetDTO(Budget budget);

    Budget toBudget(BudgetDTO budgetDTO);
}

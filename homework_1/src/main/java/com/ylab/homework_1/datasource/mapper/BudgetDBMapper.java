package com.ylab.homework_1.datasource.mapper;

import com.ylab.homework_1.datasource.model.BudgetDB;
import com.ylab.homework_1.domain.model.Budget;

import java.util.Optional;

public interface BudgetDBMapper {
    Optional<Budget> toBudget(BudgetDB budgetDB);

    Optional<BudgetDB> toBudgetDB(Budget budget);
}

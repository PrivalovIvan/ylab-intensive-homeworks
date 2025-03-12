package com.ylab.homework_1;

import com.ylab.homework_1.datasource.mapper.BudgetDBMapperImpl;
import com.ylab.homework_1.datasource.model.BudgetDB;
import com.ylab.homework_1.domain.model.Budget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

class BudgetDBMapperImplTest {
    private BudgetDBMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new BudgetDBMapperImpl();
    }

    @Test
    void toBudget_convertsBudgetDBToBudget() {
        BudgetDB budgetDB = new BudgetDB("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.valueOf(200));

        Optional<Budget> result = mapper.toBudget(budgetDB);

        Assertions.assertThat(result).isPresent();
        Budget budget = result.get();
        Assertions.assertThat(budget.getEmail()).isEqualTo("user@example.com");
        Assertions.assertThat(budget.getYearMonth()).isEqualTo(YearMonth.of(2025, 3));
        Assertions.assertThat(budget.getBudget()).isEqualTo(BigDecimal.valueOf(1000));
        Assertions.assertThat(budget.getSpent()).isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void toBudget_returnsEmptyForNull() {
        Optional<Budget> result = mapper.toBudget(null);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void toBudgetDB_convertsBudgetToBudgetDB() {
        Budget budget = new Budget("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.valueOf(200));

        Optional<BudgetDB> result = mapper.toBudgetDB(budget);

        Assertions.assertThat(result).isPresent();
        BudgetDB budgetDB = result.get();
        Assertions.assertThat(budgetDB.getEmail()).isEqualTo("user@example.com");
        Assertions.assertThat(budgetDB.getYearMonth()).isEqualTo(YearMonth.of(2025, 3));
        Assertions.assertThat(budgetDB.getBudget()).isEqualTo(BigDecimal.valueOf(1000));
        Assertions.assertThat(budgetDB.getSpent()).isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void toBudgetDB_returnsEmptyForNull() {
        Optional<BudgetDB> result = mapper.toBudgetDB(null);

        Assertions.assertThat(result).isEmpty();
    }
}
package com.ylab.homework_1;

import com.ylab.homework_1.datasource.mapper.BudgetDBMapperImpl;
import com.ylab.homework_1.datasource.model.BudgetDB;
import com.ylab.homework_1.domain.model.Budget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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

        assertThat(result).isPresent();
        Budget budget = result.get();
        assertThat(budget.getEmail()).isEqualTo("user@example.com");
        assertThat(budget.getYearMonth()).isEqualTo(YearMonth.of(2025, 3));
        assertThat(budget.getBudget()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(budget.getSpent()).isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void toBudget_returnsEmptyForNull() {
        Optional<Budget> result = mapper.toBudget(null);

        assertThat(result).isEmpty();
    }

    @Test
    void toBudgetDB_convertsBudgetToBudgetDB() {
        Budget budget = new Budget("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.valueOf(200));

        Optional<BudgetDB> result = mapper.toBudgetDB(budget);

        assertThat(result).isPresent();
        BudgetDB budgetDB = result.get();
        assertThat(budgetDB.getEmail()).isEqualTo("user@example.com");
        assertThat(budgetDB.getYearMonth()).isEqualTo(YearMonth.of(2025, 3));
        assertThat(budgetDB.getBudget()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(budgetDB.getSpent()).isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void toBudgetDB_returnsEmptyForNull() {
        Optional<BudgetDB> result = mapper.toBudgetDB(null);

        assertThat(result).isEmpty();
    }
}
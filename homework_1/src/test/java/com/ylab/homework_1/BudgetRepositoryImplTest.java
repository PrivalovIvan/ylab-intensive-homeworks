package com.ylab.homework_1;

import com.ylab.homework_1.datasource.mapper.BudgetDBMapper;
import com.ylab.homework_1.datasource.model.BudgetDB;
import com.ylab.homework_1.datasource.repository.BudgetRepositoryImpl;
import com.ylab.homework_1.domain.model.Budget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

class BudgetRepositoryImplTest {
    private BudgetDBMapper budgetDBMapper;
    private BudgetRepositoryImpl budgetRepository;

    @BeforeEach
    void setUp() {
        budgetDBMapper = Mockito.mock(BudgetDBMapper.class);
        budgetRepository = new BudgetRepositoryImpl(budgetDBMapper);
    }

    @Test
    void save_addsNewBudget() {
        Budget budget = new Budget("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.ZERO);
        BudgetDB budgetDB = new BudgetDB("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.ZERO);
        Mockito.when(budgetDBMapper.toBudgetDB(budget)).thenReturn(Optional.of(budgetDB));
        Mockito.when(budgetDBMapper.toBudget(budgetDB)).thenReturn(Optional.of(budget));

        budgetRepository.save(budget);

        Optional<Budget> savedBudget = budgetRepository.findByUserAndMonth("user@example.com", YearMonth.of(2025, 3));
        Assertions.assertThat(savedBudget).isPresent();
        Assertions.assertThat(savedBudget.get().getBudget()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    void findByUserAndMonth_returnsBudgetIfExists() {
        Budget budget = new Budget("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.valueOf(200));
        BudgetDB budgetDB = new BudgetDB("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.valueOf(200));
        Mockito.when(budgetDBMapper.toBudgetDB(budget)).thenReturn(Optional.of(budgetDB));
        Mockito.when(budgetDBMapper.toBudget(budgetDB)).thenReturn(Optional.of(budget));
        budgetRepository.save(budget);

        Optional<Budget> result = budgetRepository.findByUserAndMonth("user@example.com", YearMonth.of(2025, 3));

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get()).isEqualTo(budget);
    }

    @Test
    void findByUserAndMonth_returnsEmptyIfNotFound() {
        Optional<Budget> result = budgetRepository.findByUserAndMonth("user@example.com", YearMonth.of(2025, 3));

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void delete_removesBudget() {
        Budget budget = new Budget("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.ZERO);
        BudgetDB budgetDB = new BudgetDB("user@example.com", YearMonth.of(2025, 3), BigDecimal.valueOf(1000), BigDecimal.ZERO);
        Mockito.when(budgetDBMapper.toBudgetDB(budget)).thenReturn(Optional.of(budgetDB));
        budgetRepository.save(budget);

        budgetRepository.delete("user@example.com", YearMonth.of(2025, 3));

        Optional<Budget> result = budgetRepository.findByUserAndMonth("user@example.com", YearMonth.of(2025, 3));
        Assertions.assertThat(result).isEmpty();
    }
}
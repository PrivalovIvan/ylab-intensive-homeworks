package com.ylab.homework_1;

import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.infrastructure.mapper.BudgetMapper;
import com.ylab.homework_1.infrastructure.repository.BudgetRepositoryImpl;
import com.ylab.homework_1.infrastructure.service.BudgetServiceImpl;
import com.ylab.homework_1.infrastructure.service.NotificationServiceImpl;
import com.ylab.homework_1.usecase.dto.BudgetDTO;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class BudgetServiceImplTest {
    private static BudgetServiceImpl budgetService;

    @BeforeAll
    static void setUp() throws Exception {
        PostgresDataSource.initDB(TestContainerConfig.getProperties());

        try (var connection = PostgresDataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/migration/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        }

        budgetService = new BudgetServiceImpl(new BudgetRepositoryImpl(), new NotificationServiceImpl());
    }

    @Test
    void testCreateAndRetrieveBudget() throws SQLException {
        BudgetDTO budget = BudgetDTO.builder()
                .email("test@example.com")
                .yearMonth(YearMonth.of(2025, 3))
                .budget(BigDecimal.valueOf(1000))
                .spent(BigDecimal.ZERO)
                .build();
        budgetService.createBudget(budget);

        BudgetDTO retrievedBudget = BudgetMapper.toBudgetDTO.apply(budgetService.getBudget("test@example.com", YearMonth.of(2025, 3)).get());
        assertNotNull(retrievedBudget, "Budget should not be null");
        assertEquals(new BigDecimal("1000.00"), retrievedBudget.getBudget(), "Budget amount should match");
        assertEquals(new BigDecimal("0.00"), retrievedBudget.getSpent(), "Spent amount should be zero");
    }

    @Test
    void testAddExpenseAndCheckExceeded() throws SQLException {
        BudgetDTO budget = BudgetDTO.builder()
                .email("test@example.com")
                .yearMonth(YearMonth.of(2025, 5))
                .budget(BigDecimal.valueOf(500))
                .spent(BigDecimal.ZERO)
                .build();
        budgetService.createBudget(budget);

        budgetService.addExpense("test@example.com", YearMonth.of(2025, 5), BigDecimal.valueOf(600));

        BudgetDTO updatedBudget = BudgetMapper.toBudgetDTO.apply(budgetService.getBudget("test@example.com", YearMonth.of(2025, 5)).get());
        assertEquals(new BigDecimal("600.00"), updatedBudget.getSpent(), "Spent amount should match");
        assertTrue(BudgetMapper.toBudget.apply(updatedBudget).isExceeded(), "Budget should be exceeded");
    }
}

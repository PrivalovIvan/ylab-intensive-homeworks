package com.ylab.homework_1;

import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.infrastructure.repository.GoalRepositoryImpl;
import com.ylab.homework_1.infrastructure.service.GoalServiceImpl;
import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.service.NotificationService;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
class GoalServiceImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("finance_db")
            .withUsername("finance_user")
            .withPassword("finance_password");

    private static GoalServiceImpl goalService;
    private static NotificationService notificationService;

    @BeforeAll
    static void setUp() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("db.url", postgres.getJdbcUrl());
        properties.setProperty("db.user", postgres.getUsername());
        properties.setProperty("db.password", postgres.getPassword());
        properties.setProperty("liquibase.change-log", "db/migration/changelog.xml");
        properties.setProperty("liquibase.default-schema", "finance");

        PostgresDataSource.initDB(properties);

        try (var connection = PostgresDataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/migration/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        }

        notificationService = mock(NotificationService.class);
        goalService = new GoalServiceImpl(new GoalRepositoryImpl(), notificationService);
    }

    @Test
    void testCreateAndFindGoals() throws SQLException {
        GoalDTO goal = new GoalDTO("test@example.com", "New Car", BigDecimal.valueOf(10000), BigDecimal.ZERO);
        goalService.createGoal(goal);

        List<GoalDTO> goals = goalService.getUserGoals("test@example.com");
        assertFalse(goals.isEmpty(), "Goals list should not be empty");
        assertEquals(2, goals.size(), "Should return one goal");
        assertEquals("New Car", goals.get(0).getTitle(), "Goal title should match");
    }

    @Test
    void testUpdateGoal() throws SQLException {
        GoalDTO goal = new GoalDTO("test@example.com", "New Car", BigDecimal.valueOf(10000), BigDecimal.ZERO);
        goalService.createGoal(goal);

        GoalDTO updatedGoal = new GoalDTO("test@example.com", "New Car", BigDecimal.valueOf(15000), BigDecimal.valueOf(5000));
        goalService.updateGoal(updatedGoal);

        GoalDTO retrievedGoal = goalService.getGoalByName("test@example.com", "New Car");
        assertEquals(new BigDecimal("15000.00"), retrievedGoal.getTargetAmount(), "Target amount should be updated");
        assertEquals(new BigDecimal("5000.00"), retrievedGoal.getSavedAmount(), "Saved amount should be updated");
    }

    @Test
    void testDeleteGoal() throws SQLException {
        GoalDTO goal = new GoalDTO("test@example.com", "New Car", BigDecimal.valueOf(10000), BigDecimal.ZERO);
        goalService.createGoal(goal);
        goalService.deleteGoal("test@example.com", "New Car");

        List<GoalDTO> goals = goalService.getUserGoals("test@example.com");
        assertFalse(goals.isEmpty(), "Goals list should be empty after deletion");
    }

    @Test
    void testCheckAndNotify() throws SQLException {
        GoalDTO goal = new GoalDTO("test@example.com", "Vacation", BigDecimal.valueOf(5000), BigDecimal.valueOf(5000));
        goalService.createGoal(goal);
        goalService.checkAndNotify("test@example.com", "Vacation");

        verify(notificationService, times(1)).send("test@example.com", "Goal Vacation is completed");
    }
}

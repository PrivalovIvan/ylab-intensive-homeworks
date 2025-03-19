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
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
class GoalServiceImplTest {

    private static GoalServiceImpl goalService;
    private static NotificationService notificationService;

    @BeforeAll
    static void setUp() throws Exception {
        PostgresDataSource.initDB(TestContainerConfig.getProperties());

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
        GoalDTO goal = GoalDTO.builder()
                .email("test@example.com")
                .title("New Car")
                .targetAmount(BigDecimal.valueOf(10000))
                .savedAmount(BigDecimal.ZERO)
                .build();
        goalService.createGoal(goal);

        List<GoalDTO> goals = goalService.getUserGoals("test@example.com");
        assertFalse(goals.isEmpty(), "Goals list should not be empty");
        assertEquals(2, goals.size(), "Should return one goal");
        assertEquals("New Car", goals.get(0).getTitle(), "Goal title should match");
    }

    @Test
    void testUpdateGoal() throws SQLException {
        GoalDTO goal = GoalDTO.builder()
                .email("test@example.com")
                .title("New Car")
                .targetAmount(BigDecimal.valueOf(10000))
                .savedAmount(BigDecimal.ZERO)
                .build();
        goalService.createGoal(goal);

        GoalDTO updatedGoal = GoalDTO.builder()
                .email("test@example.com")
                .title("New Car")
                .targetAmount(BigDecimal.valueOf(15000))
                .savedAmount(BigDecimal.valueOf(5000))
                .build();
        goalService.updateGoal(updatedGoal);

        GoalDTO retrievedGoal = goalService.getGoalByName("test@example.com", "New Car");
        assertEquals(new BigDecimal("15000.00"), retrievedGoal.getTargetAmount(), "Target amount should be updated");
        assertEquals(new BigDecimal("5000.00"), retrievedGoal.getSavedAmount(), "Saved amount should be updated");
    }

    @Test
    void testDeleteGoal() throws SQLException {
        GoalDTO goal = GoalDTO.builder()
                .email("test@example.com")
                .title("New Car")
                .targetAmount(BigDecimal.valueOf(10000))
                .savedAmount(BigDecimal.ZERO)
                .build();
        goalService.createGoal(goal);
        goalService.createGoal(goal);
        goalService.deleteGoal("test@example.com", "New Car");

        List<GoalDTO> goals = goalService.getUserGoals("test@example.com");
        assertFalse(goals.isEmpty(), "Goals list should be empty after deletion");
    }

    @Test
    void testCheckAndNotify() throws SQLException {
        GoalDTO goal = GoalDTO.builder()
                .email("test@example.com")
                .title("Vacation")
                .targetAmount(BigDecimal.valueOf(5000))
                .savedAmount(BigDecimal.valueOf(5000))
                .build();
        goalService.createGoal(goal);
        goalService.createGoal(goal);
        goalService.checkAndNotify("test@example.com", "Vacation");

        verify(notificationService, times(1)).send("test@example.com", "Goal Vacation is completed");
    }
}

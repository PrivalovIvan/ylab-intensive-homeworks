package com.ylab.homework_1;

import com.ylab.homework_1.datasource.mapper.GoalDBMapperImpl;
import com.ylab.homework_1.datasource.model.GoalDB;
import com.ylab.homework_1.domain.model.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

class GoalDBMapperImplTest {
    private GoalDBMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new GoalDBMapperImpl();
    }

    @Test
    void toGoal_convertsGoalDBToGoal() {
        GoalDB goalDB = new GoalDB("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500));

        Optional<Goal> result = mapper.toGoal(goalDB);

        Assertions.assertThat(result).isPresent();
        Goal goal = result.get();
        Assertions.assertThat(goal.getEmail()).isEqualTo("user@example.com");
        Assertions.assertThat(goal.getTitle()).isEqualTo("Vacation");
        Assertions.assertThat(goal.getTargetAmount()).isEqualTo(BigDecimal.valueOf(1000));
        Assertions.assertThat(goal.getSavedAmount()).isEqualTo(BigDecimal.valueOf(500));
    }

    @Test
    void toGoal_returnsEmptyForNull() {
        Optional<Goal> result = mapper.toGoal(null);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void toGoalDB_convertsGoalToGoalDB() {
        Goal goal = new Goal("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500));

        Optional<GoalDB> result = mapper.toGoalDB(goal);

        Assertions.assertThat(result).isPresent();
        GoalDB goalDB = result.get();
        Assertions.assertThat(goalDB.getEmail()).isEqualTo("user@example.com");
        Assertions.assertThat(goalDB.getTitle()).isEqualTo("Vacation");
        Assertions.assertThat(goalDB.getTargetAmount()).isEqualTo(BigDecimal.valueOf(1000));
        Assertions.assertThat(goalDB.getSavedAmount()).isEqualTo(BigDecimal.valueOf(500));
    }

    @Test
    void toGoalDB_returnsEmptyForNull() {
        Optional<GoalDB> result = mapper.toGoalDB(null);

        Assertions.assertThat(result).isEmpty();
    }
}
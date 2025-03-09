package com.ylab.homework_1;

import com.ylab.homework_1.datasource.mapper.GoalDBMapperImpl;
import com.ylab.homework_1.datasource.model.GoalDB;
import com.ylab.homework_1.domain.model.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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

        assertThat(result).isPresent();
        Goal goal = result.get();
        assertThat(goal.getEmail()).isEqualTo("user@example.com");
        assertThat(goal.getTitle()).isEqualTo("Vacation");
        assertThat(goal.getTargetAmount()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(goal.getSavedAmount()).isEqualTo(BigDecimal.valueOf(500));
    }

    @Test
    void toGoal_returnsEmptyForNull() {
        Optional<Goal> result = mapper.toGoal(null);

        assertThat(result).isEmpty();
    }

    @Test
    void toGoalDB_convertsGoalToGoalDB() {
        Goal goal = new Goal("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500));

        Optional<GoalDB> result = mapper.toGoalDB(goal);

        assertThat(result).isPresent();
        GoalDB goalDB = result.get();
        assertThat(goalDB.getEmail()).isEqualTo("user@example.com");
        assertThat(goalDB.getTitle()).isEqualTo("Vacation");
        assertThat(goalDB.getTargetAmount()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(goalDB.getSavedAmount()).isEqualTo(BigDecimal.valueOf(500));
    }

    @Test
    void toGoalDB_returnsEmptyForNull() {
        Optional<GoalDB> result = mapper.toGoalDB(null);

        assertThat(result).isEmpty();
    }
}
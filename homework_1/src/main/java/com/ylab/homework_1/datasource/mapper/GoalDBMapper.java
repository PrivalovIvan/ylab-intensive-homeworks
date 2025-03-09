package com.ylab.homework_1.datasource.mapper;

import com.ylab.homework_1.datasource.model.GoalDB;
import com.ylab.homework_1.domain.model.Goal;

import java.util.Optional;

public interface GoalDBMapper {
    Optional<Goal> toGoal(GoalDB goal);

    Optional<GoalDB> toGoalDB(Goal goal);
}

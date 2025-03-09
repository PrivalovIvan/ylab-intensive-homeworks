package com.ylab.homework_1.datasource.mapper;

import com.ylab.homework_1.datasource.model.GoalDB;
import com.ylab.homework_1.domain.model.Goal;

import java.util.Optional;

public class GoalDBMapperImpl implements GoalDBMapper {
    @Override
    public Optional<Goal> toGoal(GoalDB goal) {
        return Optional.ofNullable(goal)
                .map(goal1 -> new Goal(
                        goal1.getEmail(),
                        goal1.getTitle(),
                        goal1.getTargetAmount(),
                        goal1.getSavedAmount()
                ));
    }

    @Override
    public Optional<GoalDB> toGoalDB(Goal goal) {
        return Optional.ofNullable(goal)
                .map(goal1 -> new GoalDB(
                        goal1.getEmail(),
                        goal1.getTitle(),
                        goal1.getTargetAmount(),
                        goal1.getSavedAmount()
                ));
    }
}

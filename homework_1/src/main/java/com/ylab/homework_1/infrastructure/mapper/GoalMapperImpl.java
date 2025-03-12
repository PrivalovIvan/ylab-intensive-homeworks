package com.ylab.homework_1.infrastructure.mapper;

import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.mapper.GoalMapper;

public class GoalMapperImpl implements GoalMapper {
    @Override
    public Goal toGoal(GoalDTO goal) {
        if (goal == null) {
            throw new IllegalArgumentException("GoalDTO cannot be null");
        }
        return new Goal(
                goal.getEmail(),
                goal.getTitle(),
                goal.getTargetAmount(),
                goal.getSavedAmount()
        );
    }

    @Override
    public GoalDTO toGoalDTO(Goal goal) {
        if (goal == null) {
            throw new IllegalArgumentException("Goal cannot be null");
        }
        return new GoalDTO(
                goal.getEmail(),
                goal.getTitle(),
                goal.getTargetAmount(),
                goal.getSavedAmount()
        );
    }
}

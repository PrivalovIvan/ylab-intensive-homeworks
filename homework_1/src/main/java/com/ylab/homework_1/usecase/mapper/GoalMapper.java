package com.ylab.homework_1.usecase.mapper;

import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.usecase.dto.GoalDTO;

public interface GoalMapper {
    Goal toGoal(GoalDTO goal);

    GoalDTO toGoalDTO(Goal goal);
}

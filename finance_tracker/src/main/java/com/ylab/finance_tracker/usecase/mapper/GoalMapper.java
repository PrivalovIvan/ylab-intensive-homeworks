package com.ylab.finance_tracker.usecase.mapper;

import com.ylab.finance_tracker.domain.model.Goal;
import com.ylab.finance_tracker.usecase.dto.GoalDTO;
import org.mapstruct.Mapper;

@Mapper
public interface GoalMapper {
    GoalDTO toGoalDTO(Goal goal);

    Goal toGoal(GoalDTO goalDTO);
}

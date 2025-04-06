package com.ylab.finance_tracker_spring.mapper;

import com.ylab.finance_tracker_spring.domain.model.Goal;
import com.ylab.finance_tracker_spring.dto.GoalDTO;
import org.mapstruct.Mapper;

@Mapper
public interface GoalMapper {
    GoalDTO toGoalDTO(Goal goal);

    Goal toGoal(GoalDTO goalDTO);
}

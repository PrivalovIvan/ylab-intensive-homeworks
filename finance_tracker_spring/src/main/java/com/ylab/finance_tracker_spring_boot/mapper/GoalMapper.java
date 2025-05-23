package com.ylab.finance_tracker_spring_boot.mapper;

import com.ylab.finance_tracker_spring_boot.domain.model.Goal;
import com.ylab.finance_tracker_spring_boot.dto.GoalDTO;
import org.mapstruct.Mapper;

@Mapper
public interface GoalMapper {
    GoalDTO toGoalDTO(Goal goal);

    Goal toGoal(GoalDTO goalDTO);
}

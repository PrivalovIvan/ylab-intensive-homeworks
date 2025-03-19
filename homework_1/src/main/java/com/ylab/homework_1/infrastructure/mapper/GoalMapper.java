package com.ylab.homework_1.infrastructure.mapper;

import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.mapper.Mapper;

public class GoalMapper {
    public static final Mapper<Goal, GoalDTO> toGoal = (goalDTO) ->
            new Goal(goalDTO.getUuid(), goalDTO.getEmail(), goalDTO.getTitle(), goalDTO.getTargetAmount(), goalDTO.getSavedAmount());

    public static final Mapper<GoalDTO, Goal> toGoalDTO = (goal) ->
            new GoalDTO(goal.getUuid(), goal.getEmail(), goal.getTitle(), goal.getTargetAmount(), goal.getSavedAmount());

}

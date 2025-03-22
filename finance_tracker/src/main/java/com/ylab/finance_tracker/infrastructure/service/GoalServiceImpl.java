package com.ylab.finance_tracker.infrastructure.service;

import com.ylab.finance_tracker.domain.model.Goal;
import com.ylab.finance_tracker.domain.repository.GoalRepository;
import com.ylab.finance_tracker.usecase.dto.GoalDTO;
import com.ylab.finance_tracker.usecase.mapper.GoalMapper;
import com.ylab.finance_tracker.usecase.service.GoalService;
import com.ylab.finance_tracker.usecase.service.NotificationService;
import org.mapstruct.factory.Mappers;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final NotificationService notificationService;
    private final GoalMapper goalMapper = Mappers.getMapper(GoalMapper.class);

    public GoalServiceImpl(GoalRepository goalRepository, NotificationService notificationService) {
        this.goalRepository = goalRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void createGoal(GoalDTO goal) throws SQLException {
        if (getGoalByName(goal.getEmail(), goal.getTitle()) != null) return;
        goalRepository.save(goalMapper.toGoal(goal));
    }

    @Override
    public void updateGoal(GoalDTO goal) throws SQLException {
        Optional<Goal> existingGoal = goalRepository.findByName(goal.getEmail(), goal.getTitle());
        if (existingGoal.isPresent()) {
            Goal updatedGoal = new Goal(
                    goal.getUuid(),
                    goal.getEmail(),
                    goal.getTitle(),
                    goal.getTargetAmount(),
                    goal.getSavedAmount()
            );
            goalRepository.update(updatedGoal);
        } else {
            throw new IllegalArgumentException("Goal " + goal.getTitle() + " not found");
        }
    }

    @Override
    public GoalDTO getGoalByName(String email, String goalName) throws SQLException {
        return goalRepository.findByName(email, goalName)
                .map(goalMapper::toGoalDTO)
                .orElse(null);
    }

    @Override
    public List<GoalDTO> getUserGoals(String email) throws SQLException {
        return goalRepository.findAllByUser(email).stream()
                .map(goalMapper::toGoalDTO)
                .toList();
    }

    @Override
    public void deleteGoal(String email, String name) throws SQLException {
        goalRepository.delete(email, name);
    }

    @Override
    public void checkAndNotify(String email, String goalName) throws SQLException {
        goalRepository.findByName(email, goalName).ifPresent(goal -> {
            if (goal.getSavedAmount().compareTo(goal.getTargetAmount()) >= 0) {
                notificationService.send(email, "Goal " + goalName + " is completed");
            }
        });
    }

}
package com.ylab.homework_1.application.service;

import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.domain.repository.GoalRepository;
import com.ylab.homework_1.domain.service.GoalService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;

    @Override
    public void setGoal(String email, String name, BigDecimal targetAmount) {
        goalRepository.save(new Goal(email, name, targetAmount, BigDecimal.ZERO));
    }

    @Override
    public List<Goal> getUserGoals(String email) {
        return goalRepository.findAllByUser(email);
    }

    @Override
    public void updateGoalProgress(String email, String name, BigDecimal amount) {
        goalRepository.findByName(email, name).ifPresent(goal -> {
            goal.setSavedAmount(goal.getSavedAmount().add(amount));
            goalRepository.updateProgress(email, name, goal);
            if (goal.isAchieved()) System.out.println("Goal: " + goal.getTitle() + " Achieved!");
        });

    }

    @Override
    public void deleteGoal(String email, String name) {
        goalRepository.delete(email, name);
    }
}

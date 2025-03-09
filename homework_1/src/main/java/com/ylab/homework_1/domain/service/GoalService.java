package com.ylab.homework_1.domain.service;

import com.ylab.homework_1.domain.model.Goal;

import java.math.BigDecimal;
import java.util.List;

public interface GoalService {
    void setGoal(String email, String name, BigDecimal targetAmount);
    List<Goal> getUserGoals(String email);
    void updateGoalProgress(String email, String name, BigDecimal amount);
    void deleteGoal(String email, String name);
}

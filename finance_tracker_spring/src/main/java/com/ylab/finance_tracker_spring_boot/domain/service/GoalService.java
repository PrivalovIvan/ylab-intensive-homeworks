package com.ylab.finance_tracker_spring_boot.domain.service;

import com.ylab.finance_tracker_spring_boot.dto.GoalDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface GoalService {
    void createGoal(GoalDTO goal) throws SQLException;

    void updateGoal(String name, BigDecimal amount) throws SQLException;

    GoalDTO getGoalByName(String email, String goalName) throws SQLException;

    List<GoalDTO> getUserGoals(String email) throws SQLException;

    void deleteGoal(String email, String name) throws SQLException;

}

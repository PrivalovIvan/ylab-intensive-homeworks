package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.dto.TransactionDTO;

import java.sql.SQLException;
import java.util.List;

public interface GoalService {
    void createGoal(GoalDTO goal) throws SQLException;

    void updateGoal(GoalDTO goal) throws SQLException;

    GoalDTO getGoalByName(String email, String goalName) throws SQLException;

    List<GoalDTO> getUserGoals(String email) throws SQLException;

    void deleteGoal(String email, String name) throws SQLException;

    void checkAndNotify(String email, String goalName) throws SQLException;

    void processIncome(TransactionDTO transactionDTO) throws SQLException;

}

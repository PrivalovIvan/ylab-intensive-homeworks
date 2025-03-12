package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.dto.TransactionDTO;

import java.util.List;

public interface GoalService {
    void createGoal(GoalDTO goal);

    void updateGoal(GoalDTO goal);

    GoalDTO getGoalByName(String email, String goalName);

    List<GoalDTO> getUserGoals(String email);

    void deleteGoal(String email, String name);

    void checkAndNotify(String email, String goalName);

    void processIncome(TransactionDTO transactionDTO);

}

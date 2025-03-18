package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.infrastructure.mapper.GoalMapper;
import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.repository.GoalRepository;
import com.ylab.homework_1.usecase.service.GoalService;
import com.ylab.homework_1.usecase.service.NotificationService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final NotificationService notificationService;

    public GoalServiceImpl(GoalRepository goalRepository, NotificationService notificationService) {
        this.goalRepository = goalRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void createGoal(GoalDTO goal) throws SQLException {
        goalRepository.save(GoalMapper.toGoal.apply(goal));
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
                .map(GoalMapper.toGoalDTO::apply)
                .orElseThrow(() -> new IllegalArgumentException("Goal " + goalName + " not found"));
    }

    @Override
    public List<GoalDTO> getUserGoals(String email) throws SQLException {
        return goalRepository.findAllByUser(email).stream()
                .map(GoalMapper.toGoalDTO::apply)
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

    @Override
    public void processIncome(TransactionDTO transactionDTO) throws SQLException {
        Optional<GoalDTO> goalOpt = Optional.ofNullable(getGoalByName(transactionDTO.getEmail(), transactionDTO.getNameGoal()));
        goalOpt.ifPresent(goalDTO -> {
            BigDecimal newSavedAmount = goalDTO.getSavedAmount().add(transactionDTO.getAmount());
            GoalDTO updatedGoal = new GoalDTO(goalDTO.getUuid() ,goalDTO.getEmail(), goalDTO.getTitle(), goalDTO.getTargetAmount(), newSavedAmount);
            try {
                updateGoal(updatedGoal);
                checkAndNotify(transactionDTO.getEmail(), transactionDTO.getNameGoal());
            } catch (SQLException e) {
                throw new RuntimeException("Failed to process income: " + e.getMessage(), e);
            }
        });
    }
}
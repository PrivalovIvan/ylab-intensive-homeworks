package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.infrastructure.mapper.GoalMapper;
import com.ylab.homework_1.usecase.dto.GoalDTO;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.repository.GoalRepository;
import com.ylab.homework_1.usecase.service.GoalService;
import com.ylab.homework_1.usecase.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final NotificationService notificationService;

    @Override
    public void createGoal(GoalDTO goal) {
        goalRepository.save(GoalMapper.toGoal.apply(goal));
    }

    @Override
    public void updateGoal(GoalDTO goal) {
        goalRepository.update(GoalMapper.toGoal.apply(goal));
    }

    @Override
    public GoalDTO getGoalByName(String email, String goalName) {
        return goalRepository.findByName(email, goalName).map(GoalMapper.toGoalDTO::apply).orElseThrow(
                () -> new IllegalArgumentException("Goal " + goalName + " not found")
        );
    }

    @Override
    public List<GoalDTO> getUserGoals(String email) {
        return goalRepository.findAllByUser(email).stream()
                .map(GoalMapper.toGoalDTO::apply)
                .toList();
    }

    @Override
    public void deleteGoal(String email, String name) {
        goalRepository.delete(email, name);
    }

    @Override
    public void checkAndNotify(String email, String goalName) {
        goalRepository.findByName(email, goalName).ifPresent(goal -> {
            if (goal.getSavedAmount().compareTo(goal.getTargetAmount()) >= 0) {
                notificationService.send(email, "Goal " + goalName + " is completed");
            }
        });
    }

    @Override
    public void processIncome(TransactionDTO transactionDTO) {
        Optional.ofNullable(getGoalByName(transactionDTO.getEmail(), transactionDTO.getNameGoal()))
                .ifPresent(goalDTO -> {
                    goalDTO.setSavedAmount(goalDTO.getSavedAmount().add(transactionDTO.getAmount()));
                    updateGoal(goalDTO);
                    checkAndNotify(transactionDTO.getEmail(), transactionDTO.getNameGoal());
                });
    }
}

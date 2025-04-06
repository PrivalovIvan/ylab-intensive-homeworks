package com.ylab.finance_tracker_spring_boot.service;

import com.ylab.finance_tracker_spring_boot.domain.model.Goal;
import com.ylab.finance_tracker_spring_boot.domain.repository.GoalRepository;
import com.ylab.finance_tracker_spring_boot.domain.service.GoalService;
import com.ylab.finance_tracker_spring_boot.dto.GoalDTO;
import com.ylab.finance_tracker_spring_boot.mapper.GoalMapper;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper = Mappers.getMapper(GoalMapper.class);
    private final AuthService authService;

    @Override
    public void createGoal(GoalDTO goal) throws SQLException {
        if (getGoalByName(goal.getEmail(), goal.getTitle()) != null) return;
        goalRepository.save(goalMapper.toGoal(goal));
    }

    @Override
    public void updateGoal(String name, BigDecimal amount) throws SQLException {
        Optional<Goal> existingGoal = goalRepository.findByName(authService.getCurrentUser().getEmail(), name);
        if (existingGoal.isPresent()) {
            Goal goal = existingGoal.get();
            BigDecimal newSavedAmount = goal.getSavedAmount().add(amount);
            goal.setSavedAmount(newSavedAmount);
            goalRepository.update(goal);
        } else {
            throw new IllegalArgumentException("Goal " + name + " not found");
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
}
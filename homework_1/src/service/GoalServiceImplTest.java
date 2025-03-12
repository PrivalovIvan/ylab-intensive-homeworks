package com.ylab.homework_1;

import com.ylab.homework_1.infrastructure.service.GoalServiceImpl;
import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.domain.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class GoalServiceImplTest {
    private GoalRepository goalRepository;
    private GoalServiceImpl goalService;

    @BeforeEach
    void setUp() {
        goalRepository = mock(GoalRepository.class);
        goalService = new GoalServiceImpl(goalRepository);
    }

    @Test
    void setGoal_createsNewGoal() {
        String email = "user@example.com";
        String name = "Vacation";
        BigDecimal targetAmount = BigDecimal.valueOf(1000);

        goalService.setGoal(email, name, targetAmount);

        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository).save(goalCaptor.capture());
        Goal savedGoal = goalCaptor.getValue();
        assertThat(savedGoal.getEmail()).isEqualTo(email);
        assertThat(savedGoal.getTitle()).isEqualTo(name);
        assertThat(savedGoal.getTargetAmount()).isEqualTo(targetAmount);
        assertThat(savedGoal.getSavedAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void getUserGoals_returnsAllGoals() {
        String email = "user@example.com";
        List<Goal> goals = List.of(
                new Goal(email, "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500)),
                new Goal(email, "Car", BigDecimal.valueOf(5000), BigDecimal.valueOf(2000))
        );
        when(goalRepository.findAllByUser(email)).thenReturn(goals);

        List<Goal> result = goalService.getUserGoals(email);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(goals);
    }

    @Test
    void getUserGoals_returnsEmptyListIfNoGoals() {
        String email = "user@example.com";
        when(goalRepository.findAllByUser(email)).thenReturn(List.of());

        List<Goal> result = goalService.getUserGoals(email);

        assertThat(result).isEmpty();
    }

    @Test
    void updateGoalProgress_updatesProgressAndNotifiesIfAchieved() {
        String email = "user@example.com";
        String name = "Vacation";
        Goal goal = new Goal(email, name, BigDecimal.valueOf(1000), BigDecimal.valueOf(800));
        when(goalRepository.findByName(email, name)).thenReturn(Optional.of(goal));

        goalService.updateGoalProgress(email, name, BigDecimal.valueOf(300));

        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository).updateProgress(eq(email), eq(name), goalCaptor.capture());
        Goal updatedGoal = goalCaptor.getValue();
        assertThat(updatedGoal.getSavedAmount()).isEqualTo(BigDecimal.valueOf(1100)); // 800 + 300
        assertThat(updatedGoal.isAchieved()).isTrue();
    }

    @Test
    void updateGoalProgress_doesNothingIfGoalNotFound() {
        String email = "user@example.com";
        String name = "Vacation";
        when(goalRepository.findByName(email, name)).thenReturn(Optional.empty());

        goalService.updateGoalProgress(email, name, BigDecimal.valueOf(200));

        verify(goalRepository, never()).updateProgress(anyString(), anyString(), any(Goal.class));
    }

    @Test
    void updateGoalProgress_doesNotNotifyIfNotAchieved() {
        String email = "user@example.com";
        String name = "Car";
        Goal goal = new Goal(email, name, BigDecimal.valueOf(5000), BigDecimal.valueOf(2000));
        when(goalRepository.findByName(email, name)).thenReturn(Optional.of(goal));

        goalService.updateGoalProgress(email, name, BigDecimal.valueOf(1000));

        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository).updateProgress(eq(email), eq(name), goalCaptor.capture());
        Goal updatedGoal = goalCaptor.getValue();
        assertThat(updatedGoal.getSavedAmount()).isEqualTo(BigDecimal.valueOf(3000)); // 2000 + 1000
        assertThat(updatedGoal.isAchieved()).isFalse();
    }

    @Test
    void deleteGoal_callsRepositoryDelete() {
        String email = "user@example.com";
        String name = "Vacation";

        goalService.deleteGoal(email, name);

        verify(goalRepository).delete(email, name);
    }
}
//package com.ylab.homework_1;
//
//import com.ylab.homework_1.domain.model.Goal;
//import com.ylab.homework_1.domain.repository.GoalRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//class GoalServiceImplTest {
//    private GoalRepository goalRepository;
//    private GoalServiceImpl goalService;
//
//    @BeforeEach
//    void setUp() {
//        goalRepository = Mockito.mock(GoalRepository.class);
//        goalService = new GoalServiceImpl(goalRepository);
//    }
//
//    @Test
//    void setGoal_createsNewGoal() {
//        String email = "user@example.com";
//        String name = "Vacation";
//        BigDecimal targetAmount = BigDecimal.valueOf(1000);
//
//        goalService.setGoal(email, name, targetAmount);
//
//        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
//        Mockito.verify(goalRepository).save(goalCaptor.capture());
//        Goal savedGoal = goalCaptor.getValue();
//        Assertions.assertThat(savedGoal.getEmail()).isEqualTo(email);
//        Assertions.assertThat(savedGoal.getTitle()).isEqualTo(name);
//        Assertions.assertThat(savedGoal.getTargetAmount()).isEqualTo(targetAmount);
//        Assertions.assertThat(savedGoal.getSavedAmount()).isEqualTo(BigDecimal.ZERO);
//    }
//
//    @Test
//    void getUserGoals_returnsAllGoals() {
//        String email = "user@example.com";
//        List<Goal> goals = List.of(
//                new Goal(email, "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500)),
//                new Goal(email, "Car", BigDecimal.valueOf(5000), BigDecimal.valueOf(2000))
//        );
//        Mockito.when(goalRepository.findAllByUser(email)).thenReturn(goals);
//
//        List<Goal> result = goalService.getUserGoals(email);
//
//        Assertions.assertThat(result).hasSize(2);
//        Assertions.assertThat(result).containsExactlyElementsOf(goals);
//    }
//
//    @Test
//    void getUserGoals_returnsEmptyListIfNoGoals() {
//        String email = "user@example.com";
//        Mockito.when(goalRepository.findAllByUser(email)).thenReturn(List.of());
//
//        List<Goal> result = goalService.getUserGoals(email);
//
//        Assertions.assertThat(result).isEmpty();
//    }
//
//    @Test
//    void updateGoalProgress_updatesProgressAndNotifiesIfAchieved() {
//        String email = "user@example.com";
//        String name = "Vacation";
//        Goal goal = new Goal(email, name, BigDecimal.valueOf(1000), BigDecimal.valueOf(800));
//        Mockito.when(goalRepository.findByName(email, name)).thenReturn(Optional.of(goal));
//
//        goalService.updateGoalProgress(email, name, BigDecimal.valueOf(300));
//
//        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
//        Mockito.verify(goalRepository).updateProgress(ArgumentMatchers.eq(email), ArgumentMatchers.eq(name), goalCaptor.capture());
//        Goal updatedGoal = goalCaptor.getValue();
//        Assertions.assertThat(updatedGoal.getSavedAmount()).isEqualTo(BigDecimal.valueOf(1100)); // 800 + 300
//        Assertions.assertThat(updatedGoal.isAchieved()).isTrue();
//    }
//
//    @Test
//    void updateGoalProgress_doesNothingIfGoalNotFound() {
//        String email = "user@example.com";
//        String name = "Vacation";
//        Mockito.when(goalRepository.findByName(email, name)).thenReturn(Optional.empty());
//
//        goalService.updateGoalProgress(email, name, BigDecimal.valueOf(200));
//
//        Mockito.verify(goalRepository, Mockito.never()).updateProgress(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(Goal.class));
//    }
//
//    @Test
//    void updateGoalProgress_doesNotNotifyIfNotAchieved() {
//        String email = "user@example.com";
//        String name = "Car";
//        Goal goal = new Goal(email, name, BigDecimal.valueOf(5000), BigDecimal.valueOf(2000));
//        Mockito.when(goalRepository.findByName(email, name)).thenReturn(Optional.of(goal));
//
//        goalService.updateGoalProgress(email, name, BigDecimal.valueOf(1000));
//
//        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
//        Mockito.verify(goalRepository).updateProgress(ArgumentMatchers.eq(email), ArgumentMatchers.eq(name), goalCaptor.capture());
//        Goal updatedGoal = goalCaptor.getValue();
//        Assertions.assertThat(updatedGoal.getSavedAmount()).isEqualTo(BigDecimal.valueOf(3000)); // 2000 + 1000
//        Assertions.assertThat(updatedGoal.isAchieved()).isFalse();
//    }
//
//    @Test
//    void deleteGoal_callsRepositoryDelete() {
//        String email = "user@example.com";
//        String name = "Vacation";
//
//        goalService.deleteGoal(email, name);
//
//        Mockito.verify(goalRepository).delete(email, name);
//    }
//}
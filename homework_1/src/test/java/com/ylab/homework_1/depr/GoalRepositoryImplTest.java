//package com.ylab.homework_1;
//
//import com.ylab.homework_1.datasource.mapper.GoalDBMapper;
//import com.ylab.homework_1.datasource.model.GoalDB;
//import com.ylab.homework_1.datasource.repository.GoalRepositoryImpl;
//import com.ylab.homework_1.domain.model.Goal;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//class GoalRepositoryImplTest {
//    private GoalDBMapper goalDBMapper;
//    private GoalRepositoryImpl goalRepository;
//
//    @BeforeEach
//    void setUp() {
//        goalDBMapper = Mockito.mock(GoalDBMapper.class);
//        goalRepository = new GoalRepositoryImpl(goalDBMapper);
//    }
//
//    @Test
//    void save_addsNewGoal() {
//        Goal goal = new Goal("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.ZERO);
//        GoalDB goalDB = new GoalDB("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.ZERO);
//        Mockito.when(goalDBMapper.toGoalDB(goal)).thenReturn(Optional.of(goalDB));
//        Mockito.when(goalDBMapper.toGoal(goalDB)).thenReturn(Optional.of(goal));
//
//        goalRepository.save(goal);
//
//        Optional<Goal> savedGoal = goalRepository.findByName("user@example.com", "Vacation");
//        Assertions.assertThat(savedGoal).isPresent();
//        Assertions.assertThat(savedGoal.get().getTargetAmount()).isEqualTo(BigDecimal.valueOf(1000));
//    }
//
//    @Test
//    void findByName_returnsGoalIfExists() {
//        Goal goal = new Goal("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500));
//        GoalDB goalDB = new GoalDB("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500));
//        Mockito.when(goalDBMapper.toGoalDB(goal)).thenReturn(Optional.of(goalDB));
//        Mockito.when(goalDBMapper.toGoal(goalDB)).thenReturn(Optional.of(goal));
//        goalRepository.save(goal);
//
//        Optional<Goal> result = goalRepository.findByName("user@example.com", "Vacation");
//
//        Assertions.assertThat(result).isPresent();
//        Assertions.assertThat(result.get()).isEqualTo(goal);
//    }
//
//    @Test
//    void findByName_returnsEmptyIfNotFound() {
//        Optional<Goal> result = goalRepository.findByName("user@example.com", "Vacation");
//
//        Assertions.assertThat(result).isEmpty();
//    }
//
//    @Test
//    void findAllByUser_returnsAllGoals() {
//        Goal goal1 = new Goal("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.ZERO);
//        Goal goal2 = new Goal("user@example.com", "Car", BigDecimal.valueOf(5000), BigDecimal.valueOf(2000));
//        GoalDB goalDB1 = new GoalDB("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.ZERO);
//        GoalDB goalDB2 = new GoalDB("user@example.com", "Car", BigDecimal.valueOf(5000), BigDecimal.valueOf(2000));
//        Mockito.when(goalDBMapper.toGoalDB(goal1)).thenReturn(Optional.of(goalDB1));
//        Mockito.when(goalDBMapper.toGoalDB(goal2)).thenReturn(Optional.of(goalDB2));
//        Mockito.when(goalDBMapper.toGoal(goalDB1)).thenReturn(Optional.of(goal1));
//        Mockito.when(goalDBMapper.toGoal(goalDB2)).thenReturn(Optional.of(goal2));
//        goalRepository.save(goal1);
//        goalRepository.save(goal2);
//
//        List<Goal> result = goalRepository.findAllByUser("user@example.com");
//
//        Assertions.assertThat(result).hasSize(2);
//        Assertions.assertThat(result).containsExactlyInAnyOrder(goal1, goal2);
//    }
//
//    @Test
//    void updateProgress_updatesGoal() {
//        Goal goal = new Goal("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.ZERO);
//        GoalDB goalDB = new GoalDB("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.ZERO);
//        Mockito.when(goalDBMapper.toGoalDB(goal)).thenReturn(Optional.of(goalDB));
//        Mockito.when(goalDBMapper.toGoal(goalDB)).thenReturn(Optional.of(goal));
//        goalRepository.save(goal);
//
//        Goal updatedGoal = new Goal("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500));
//        GoalDB updatedGoalDB = new GoalDB("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.valueOf(500));
//        Mockito.when(goalDBMapper.toGoalDB(updatedGoal)).thenReturn(Optional.of(updatedGoalDB));
//        Mockito.when(goalDBMapper.toGoal(updatedGoalDB)).thenReturn(Optional.of(updatedGoal)); // Добавляем настройку для updatedGoalDB
//
//        goalRepository.updateProgress("user@example.com", "Vacation", updatedGoal);
//
//        Optional<Goal> result = goalRepository.findByName("user@example.com", "Vacation");
//        Assertions.assertThat(result).isPresent();
//        Assertions.assertThat(result.get().getSavedAmount()).isEqualTo(BigDecimal.valueOf(500));
//    }
//
//    @Test
//    void delete_removesGoal() {
//        Goal goal = new Goal("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.ZERO);
//        GoalDB goalDB = new GoalDB("user@example.com", "Vacation", BigDecimal.valueOf(1000), BigDecimal.ZERO);
//        Mockito.when(goalDBMapper.toGoalDB(goal)).thenReturn(Optional.of(goalDB));
//        goalRepository.save(goal);
//
//        goalRepository.delete("user@example.com", "Vacation");
//
//        Optional<Goal> result = goalRepository.findByName("user@example.com", "Vacation");
//        Assertions.assertThat(result).isEmpty();
//    }
//}
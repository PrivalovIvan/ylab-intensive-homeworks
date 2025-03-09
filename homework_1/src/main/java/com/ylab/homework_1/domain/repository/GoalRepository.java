package com.ylab.homework_1.domain.repository;

import com.ylab.homework_1.domain.model.Goal;

import java.util.List;
import java.util.Optional;

public interface GoalRepository {
    void save(Goal goal);
    Optional<Goal> findByName(String email, String name);
    List<Goal> findAllByUser(String email);
    void updateProgress(String email, String name, Goal updatedGoal);
    void delete(String email, String name);
}

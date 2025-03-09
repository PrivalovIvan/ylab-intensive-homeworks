package com.ylab.homework_1.datasource.repository;

import com.ylab.homework_1.datasource.mapper.GoalDBMapper;
import com.ylab.homework_1.datasource.model.GoalDB;
import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.domain.repository.GoalRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class GoalRepositoryImpl implements GoalRepository {
    private final GoalDBMapper goalDBMapper;
    private final Map<String, Map<String, GoalDB>> goals = new HashMap<>();

    @Override
    public void save(Goal goal) {
        goalDBMapper.toGoalDB(goal)
                .ifPresent(g -> goals.computeIfAbsent(g.getEmail(), k -> new HashMap<>())
                        .put(goal.getTitle(), g));
    }

    @Override
    public Optional<Goal> findByName(String email, String name) {
        return Optional.ofNullable(goals.getOrDefault(email, new HashMap<>()).get(name))
                .flatMap(goalDBMapper::toGoal);
    }

    @Override
    public List<Goal> findAllByUser(String email) {
        return goals.getOrDefault(email, new HashMap<>()).values().stream()
                .map(goalDBMapper::toGoal)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public void updateProgress(String email, String name, Goal updatedGoal) {
        save(updatedGoal);
    }

    @Override
    public void delete(String email, String name) {
        Optional.ofNullable(goals.get(email)).ifPresent(goal -> {
            goal.remove(name);
            if (goals.isEmpty()) goals.remove(email);
        });
    }
}

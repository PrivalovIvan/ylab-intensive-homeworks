package com.ylab.homework_1.infrastructure.repository;

import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.usecase.repository.GoalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoalRepositoryImpl implements GoalRepository {
    private final List<Goal> goals = new ArrayList<>();

    @Override
    public void save(Goal goal) {
        boolean isMatch = goals.stream().anyMatch(g -> g.getTitle().equals(goal.getTitle()));
        if (isMatch) {
            throw new IllegalArgumentException("There is already a goal with that name");
        } else {
            goals.add(goal);
        }
    }

    @Override
    public void update(Goal goal) {
        goals.stream()
                .filter(g -> g.getTitle().equals(goal.getTitle()))
                .findFirst()
                .ifPresentOrElse(g -> g.setSavedAmount(g.getSavedAmount().add(goal.getSavedAmount())),
                        () -> new IllegalArgumentException("There is no goal with that name"));
    }


    @Override
    public Optional<Goal> findByName(String email, String name) {
        return goals.stream()
                .filter(g -> g.getEmail().equals(email) && g.getTitle().equals(name))
                .findFirst();
    }

    @Override
    public List<Goal> findAllByUser(String email) {
        return goals.stream()
                .filter(g -> g.getEmail().equals(email))
                .toList();
    }

    @Override
    public void delete(String email, String name) {
        goals.removeIf(g -> g.getEmail().equals(email) && g.getTitle().equals(name));
    }
}

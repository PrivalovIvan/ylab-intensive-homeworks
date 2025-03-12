package com.ylab.homework_1.usecase.repository;

import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.usecase.dto.GoalDTO;

import java.util.List;
import java.util.Optional;

public interface GoalRepository {
    void save(Goal goal);

    void update(Goal goal);

    Optional<Goal> findByName(String email, String name);

    List<Goal> findAllByUser(String email);

    void delete(String email, String name);
}

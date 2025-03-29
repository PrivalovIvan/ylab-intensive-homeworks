package com.ylab.finance_tracker_spring.domain.repository;

import com.ylab.finance_tracker_spring.domain.model.Goal;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GoalRepository {
    void save(Goal goal) throws SQLException;

    void update(Goal goal) throws SQLException;

    Optional<Goal> findByName(String email, String name) throws SQLException;

    List<Goal> findAllByUser(String email) throws SQLException;

    void delete(String email, String name) throws SQLException;
}

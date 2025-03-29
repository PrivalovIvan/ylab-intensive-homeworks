package com.ylab.finance_tracker_spring.domain.repository;

import com.ylab.finance_tracker_spring.domain.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user) throws SQLException;

    void update(User user) throws SQLException;

    Optional<User> getByEmail(String email) throws SQLException;

    Optional<User> getById(UUID uuid) throws SQLException;

    void delete(String email) throws SQLException;

    List<User> getAll() throws SQLException;
}

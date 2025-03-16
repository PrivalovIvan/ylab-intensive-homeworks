package com.ylab.homework_1.usecase.repository;

import com.ylab.homework_1.domain.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user) throws SQLException;

    void update(User user) throws SQLException;

    Optional<User> getByEmail(String email) throws SQLException;

    Optional<User> getById(Long uuid) throws SQLException;

    void delete(String email) throws SQLException;

    List<User> getAll() throws SQLException;
}

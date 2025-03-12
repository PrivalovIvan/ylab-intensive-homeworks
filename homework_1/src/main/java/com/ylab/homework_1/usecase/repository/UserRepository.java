package com.ylab.homework_1.usecase.repository;

import com.ylab.homework_1.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);

    Optional<User> getByEmail(String email);

    Optional<User> getById(UUID uuid);

    void delete(String email);

    List<User> getAll();
}

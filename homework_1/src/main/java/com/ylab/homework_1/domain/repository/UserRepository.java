package com.ylab.homework_1.domain.repository;

import com.ylab.homework_1.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void create(User user);

    Optional<User> findByEmail(String email);

    void delete(String email);

    List<User> findAll();

    Optional<User> update(User currentUser, String newName, String newEmail, String newPassword);
}

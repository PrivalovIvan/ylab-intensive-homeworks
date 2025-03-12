package com.ylab.homework_1.infrastructure.repository;

import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.usecase.repository.UserRepository;

import java.util.*;

public class UserRepositoryImpl implements UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> getById(UUID uuid) {
        return users.values().stream()
                .filter(user -> user.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public void delete(String email) {
        getByEmail(email).ifPresent(users::remove);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

}

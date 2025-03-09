package com.ylab.homework_1.datasource.repository;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.datasource.mapper.UserDBMapper;
import com.ylab.homework_1.datasource.model.UserDB;
import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.domain.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserDBMapper userDBMapper;
    private final Map<String, UserDB> users = new HashMap<>();

    @Override
    public void create(User user) throws IllegalArgumentException {
        if (users.containsKey(user.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }
        userDBMapper.toUserDB(user).ifPresent(userDB -> users.put(user.getEmail(), userDB));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .flatMap(userDBMapper::toUser);
    }

    @Override
    public void delete(String email) {
        users.remove(email);
    }

    @Override
    public List<User> findAll() {
        return users.values().stream()
                .map(userDBMapper::toUser)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public Optional<User> update(User currentUser, String newName, String newEmail, String newPassword) throws IllegalArgumentException {
        if (currentUser == null) throw new IllegalArgumentException("Current user is null");
        UserDB userDB = users.get(currentUser.getEmail());
        if (userDB == null) throw new IllegalArgumentException("User not found");
        users.remove(currentUser.getEmail());
        UserDB updateUser = new UserDB(newName, newEmail, newPassword, currentUser.getRole());
        create(userDBMapper.toUser(updateUser).get());
        return Optional.ofNullable(updateUser).flatMap(userDBMapper::toUser);
    }
}

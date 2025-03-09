package com.ylab.homework_1.application.service;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.domain.repository.UserRepository;
import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.domain.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(String name, String email, String password, Role role) throws IllegalArgumentException {
        if (email == null) throw new IllegalArgumentException("email is required");
        if (password == null) throw new IllegalArgumentException("password is required");
        userRepository.create(new User(name, email, password, role));
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !user.getPassword().equals(password))
            throw new IllegalArgumentException("Invalid email or password");
        return user;
    }

    @Override
    public User changeName(User currentUser, String newName) {
        return userRepository.update(currentUser, newName, currentUser.getEmail(), currentUser.getPassword()).get();
    }

    @Override
    public User changeEmail(User currentUser, String newEmail) {
        return userRepository.update(currentUser, currentUser.getName(), newEmail, currentUser.getPassword()).get();
    }

    @Override
    public User changePassword(User currentUser, String newPassword) {
        return userRepository.update(currentUser, currentUser.getName(), currentUser.getEmail(), newPassword).get();
    }

    @Override
    public void delete(String email) {
        userRepository.delete(email);
    }

    @Override
    public void delete(User currentUser) {
        userRepository.delete(currentUser.getEmail());
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email is required");
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Email not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}

package com.ylab.homework_1.domain.service;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.domain.model.User;

import java.util.List;

public interface UserService {
    void register(String name, String email, String password, Role role);

    User login(String email, String password);

    User changeName(User currentUser, String newName);

    User changeEmail(User currentUser, String newEmail);

    User changePassword(User currentUser, String newPassword);

    void delete(String email);
    void delete(User currentUser);

    User findByEmail(String email);


    // Role Admin
    List<User> findAll();

}

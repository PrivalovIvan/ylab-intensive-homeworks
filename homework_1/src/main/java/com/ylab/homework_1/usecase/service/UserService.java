package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.usecase.dto.UserDTO;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface UserService {
    void register(UserDTO userDTO);

    UserDTO login(String email, String password);

    UserDTO updateUser(UUID userId, String newName, String newEmail, String newPassword);

    void delete(String email);

    UserDTO findByEmail(String email);

    UserDTO findByUuid(UUID uuid);

    List<UserDTO> findAll();
}

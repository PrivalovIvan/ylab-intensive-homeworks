package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.usecase.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    void register(UserDTO userDTO) throws SQLException;

    UserDTO login(String email, String password) throws SQLException;

    UserDTO updateUser(UUID userId, String newName, String newEmail, String newPassword) throws SQLException;

    void delete(String email) throws SQLException;

    UserDTO findByEmail(String email) throws SQLException;

    UserDTO findByUuid(UUID uuid) throws SQLException;

    List<UserDTO> findAll() throws SQLException;
}

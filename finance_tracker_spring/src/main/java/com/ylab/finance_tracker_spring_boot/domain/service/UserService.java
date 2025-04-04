package com.ylab.finance_tracker_spring_boot.domain.service;

import com.ylab.finance_tracker_spring_boot.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void register(UserDTO userDTO) throws SQLException, IllegalArgumentException;

    UserDTO login(String email, String password) throws SQLException;

    UserDTO updateUser(UUID userId, String newName, String newEmail, String newPassword) throws SQLException;

    void delete(String email) throws SQLException;

    Optional<UserDTO> findByEmail(String email) throws SQLException;

    Optional<UserDTO> findByUuid(UUID uuid) throws SQLException;

    List<UserDTO> findAll() throws SQLException;
}

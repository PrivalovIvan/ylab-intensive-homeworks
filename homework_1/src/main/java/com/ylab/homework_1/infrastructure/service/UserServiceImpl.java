package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.infrastructure.mapper.UserMapper;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.usecase.repository.UserRepository;
import com.ylab.homework_1.usecase.service.UserService;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void register(UserDTO userDTO) throws SQLException {
        User user = userRepository.getByEmail(userDTO.getEmail()).orElse(null);
        if (user == null) {
            userRepository.save(UserMapper.toUser.apply(userDTO));
        } else {
            throw new IllegalArgumentException("User already exists");
        }
    }

    @Override
    public UserDTO login(String email, String password) throws IllegalArgumentException, SQLException {
        User user = userRepository.getByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email: " + email + " not found"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return UserMapper.toUserDTO.apply(user);
    }

    @Override
    public UserDTO updateUser(Long userId, String newName, String newEmail, String newPassword) throws SQLException {
        User user = userRepository.getById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (newEmail != null && userRepository.getByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        String name = newName != null ? newName : user.getName();
        String email = newEmail != null ? newEmail : user.getEmail();
        String password = newPassword != null ? newPassword : user.getPassword();

        UserDTO updatedUser = new UserDTO(userId, name, email, password, user.getRole());
        userRepository.update(UserMapper.toUser.apply(updatedUser));
        return updatedUser;
    }

    @Override
    public void delete(String email) throws SQLException {
        userRepository.delete(email);
    }

    @Override
    public UserDTO findByEmail(String email) throws SQLException {
        return UserMapper.toUserDTO.apply(userRepository.getByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email: " + email + " not found")));
    }

    @Override
    public UserDTO findByUuid(Long id) throws SQLException {
        return UserMapper.toUserDTO.apply(userRepository.getById(id).orElseThrow(() ->
                new IllegalArgumentException("ID: " + id + " not found")));
    }

    @Override
    public List<UserDTO> findAll() throws SQLException {
        return userRepository.getAll().stream().map(UserMapper.toUserDTO::apply).toList();
    }
}

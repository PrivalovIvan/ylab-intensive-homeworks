package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.usecase.mapper.UserMapper;
import com.ylab.homework_1.usecase.repository.UserRepository;
import com.ylab.homework_1.usecase.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void register(UserDTO userDTO) {
        User user = userRepository.getByEmail(userDTO.getEmail()).orElse(null);
        if (user == null) {
            userRepository.save(userMapper.toUser(userDTO));
        } else {
            throw new IllegalArgumentException("User already exists");
        }
    }

    @Override
    public UserDTO login(String email, String password) throws IllegalArgumentException {
        User user = userRepository.getByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email: " + email + " not found"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID userId, String newName, String newEmail, String newPassword) {
        User user = userRepository.getById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (newEmail != null && userRepository.getByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        String name = newName != null ? newName : user.getName();
        String email = newEmail != null ? newEmail : user.getEmail();
        String password = newPassword != null ? newPassword : user.getPassword();

        UserDTO updatedUser = new UserDTO(userId, name, email, password, user.getRole());
        userRepository.save(userMapper.toUser(updatedUser));
        return updatedUser;
    }

    @Override
    public void delete(String email) {
        userRepository.delete(email);
    }

    @Override
    public UserDTO findByEmail(String email) {
        return userMapper.toUserDTO(userRepository.getByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email: " + email + " not found")));
    }

    @Override
    public UserDTO findByUuid(UUID uuid) {
        return userMapper.toUserDTO(userRepository.getById(uuid).orElseThrow(() ->
                new IllegalArgumentException("UUID: " + uuid + " not found")));
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.getAll().stream().map(userMapper::toUserDTO).toList();
    }
}

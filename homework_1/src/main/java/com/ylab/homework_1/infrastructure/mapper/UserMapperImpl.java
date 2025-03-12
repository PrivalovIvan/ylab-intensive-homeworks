package com.ylab.homework_1.infrastructure.mapper;

import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.usecase.mapper.UserMapper;

import java.util.Optional;

public class UserMapperImpl implements UserMapper {
    @Override
    public User toUser(UserDTO user) {
        if (user == null) {
            throw new IllegalArgumentException("UserDTO must not be null");
        }
        return new User(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }

    @Override
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }
}

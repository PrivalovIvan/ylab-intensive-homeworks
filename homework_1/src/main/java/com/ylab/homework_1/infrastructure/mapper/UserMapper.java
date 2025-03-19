package com.ylab.homework_1.infrastructure.mapper;

import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.usecase.mapper.Mapper;

public class UserMapper {
    public static final Mapper<User, UserDTO> toUser = userDTO ->
            new User(userDTO.getUuid() ,userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getRole());

    public static final Mapper<UserDTO, User> toUserDTO = user ->
            new UserDTO(user.getUuid(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
}

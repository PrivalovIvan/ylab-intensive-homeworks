package com.ylab.homework_1.usecase.mapper;

import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.usecase.dto.UserDTO;

import java.util.Optional;

public interface UserMapper {
    User toUser(UserDTO user);

    UserDTO toUserDTO(User user);
}

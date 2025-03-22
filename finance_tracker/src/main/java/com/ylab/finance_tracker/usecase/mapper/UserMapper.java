package com.ylab.finance_tracker.usecase.mapper;

import com.ylab.finance_tracker.domain.model.User;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}

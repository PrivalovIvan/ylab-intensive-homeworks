package com.ylab.finance_tracker_spring.mapper;

import com.ylab.finance_tracker_spring.domain.model.User;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}

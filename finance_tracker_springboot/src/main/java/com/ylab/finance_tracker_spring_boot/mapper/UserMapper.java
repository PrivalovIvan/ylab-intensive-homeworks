package com.ylab.finance_tracker_spring_boot.mapper;

import com.ylab.finance_tracker_spring_boot.domain.model.User;
import com.ylab.finance_tracker_spring_boot.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}

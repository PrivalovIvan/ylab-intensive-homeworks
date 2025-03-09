package com.ylab.homework_1.datasource.mapper;

import com.ylab.homework_1.datasource.model.UserDB;
import com.ylab.homework_1.domain.model.User;

import java.util.Optional;

public interface UserDBMapper {
    Optional<User> toUser(UserDB userDB);

    Optional<UserDB> toUserDB(User user);
}

package com.ylab.homework_1.datasource.mapper;

import com.ylab.homework_1.datasource.model.UserDB;
import com.ylab.homework_1.domain.model.User;

import java.util.Optional;

public class UserDBMapperImpl implements UserDBMapper {
    @Override
    public Optional<User> toUser(UserDB userDB) {
        return Optional.ofNullable(userDB)
                .map(user -> new User(userDB.getName(), userDB.getEmail(), userDB.getPassword(), userDB.getRole()));
    }

    @Override
    public Optional<UserDB> toUserDB(User user) {
        return Optional.ofNullable(user)
                .map(userDB -> new UserDB(userDB.getName(), userDB.getEmail(), userDB.getPassword(), userDB.getRole()));
    }
}

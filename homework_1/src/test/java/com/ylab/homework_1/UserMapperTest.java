package com.ylab.homework_1;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.datasource.mapper.UserDBMapper;
import com.ylab.homework_1.datasource.mapper.UserDBMapperImpl;
import com.ylab.homework_1.datasource.model.UserDB;
import com.ylab.homework_1.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    private final UserDBMapper userDBMapper = new UserDBMapperImpl();

    @Test
    void toUser_shouldMapUserDBToUser() {
        UserDB userDB = new UserDB("Alice", "alice@example.com", "password", Role.USER);
        Optional<User> user = userDBMapper.toUser(userDB);
        AssertionsForClassTypes.assertThat(user).isPresent();
        AssertionsForClassTypes.assertThat(user.get()).isEqualTo(new User("Alice", "alice@example.com", "password", Role.USER));
    }

    @Test
    void toUserDB_shouldMapUserToUserDB() {
        User user = new User("Bob", "bob@example.com", "pass123", Role.ADMIN);
        Optional<UserDB> userDB = userDBMapper.toUserDB(user);
        AssertionsForClassTypes.assertThat(userDB).isPresent();
        AssertionsForClassTypes.assertThat(userDB.get()).isEqualTo(new UserDB("Bob", "bob@example.com", "pass123", Role.ADMIN));
    }
}
package com.ylab.homework_1;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.datasource.mapper.UserDBMapper;
import com.ylab.homework_1.datasource.mapper.UserDBMapperImpl;
import com.ylab.homework_1.datasource.repository.UserRepositoryImpl;
import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class UserRepositoryImplTest {
    private UserRepository userRepository;
    private UserDBMapper userDBMapper;

    @BeforeEach
    void setUp() {
        userDBMapper = new UserDBMapperImpl();
        userRepository = new UserRepositoryImpl(userDBMapper);
    }

    @Test
    void shouldCreateUser() {
        User user = new User("John Doe", "john@example.com", "password", Role.USER);
        userRepository.create(user);

        Optional<User> foundUser = userRepository.findByEmail("john@example.com");
        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void shouldNotCreateDuplicateUser() {
        User user = new User("John Doe", "john@example.com", "password", Role.USER);
        userRepository.create(user);

        Assertions.assertThatThrownBy(() -> userRepository.create(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already exists");
    }

    @Test
    void shouldFindUserByEmail() {
        User user = new User("John Doe", "john@example.com", "password", Role.USER);
        userRepository.create(user);

        Optional<User> foundUser = userRepository.findByEmail("john@example.com");
        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void shouldDeleteUser() {
        User user = new User("John Doe", "john@example.com", "password", Role.USER);
        userRepository.create(user);

        userRepository.delete("john@example.com");
        Assertions.assertThat(userRepository.findByEmail("john@example.com")).isEmpty();
    }

    @Test
    void shouldUpdateUser() {
        User user = new User("John Doe", "john@example.com", "password", Role.USER);
        userRepository.create(user);

        Optional<User> updatedUser = userRepository.update(user, "Jane Doe", "jane@example.com", "newpass");

        Assertions.assertThat(updatedUser).isPresent();
        Assertions.assertThat(updatedUser.get().getEmail()).isEqualTo("jane@example.com");
        Assertions.assertThat(userRepository.findByEmail("john@example.com")).isEmpty();
    }
}

package com.ylab.homework_1;

import com.ylab.homework_1.infrastructure.service.UserServiceImpl;
import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.domain.model.User;
import com.ylab.homework_1.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john@example.com", "password123", Role.USER);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        doNothing().when(userRepository).create(user);
        userService.register(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        verify(userRepository, times(1)).create(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNullOnRegister() {
        assertThatThrownBy(() -> userService.register("John", null, "password", Role.USER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email is required");
    }

    @Test
    void shouldLoginSuccessfully() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        assertThat(loggedInUser).isEqualTo(user);
    }

    @Test
    void shouldThrowExceptionWhenInvalidCredentials() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.login(user.getEmail(), "wrongPassword"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void shouldDeleteUserByEmail() {
        doNothing().when(userRepository).delete(user.getEmail());
        userService.delete(user.getEmail());
        verify(userRepository, times(1)).delete(user.getEmail());
    }

    @Test
    void shouldFindUserByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User foundUser = userService.findByEmail(user.getEmail());
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findByEmail(user.getEmail()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email not found");
    }
}

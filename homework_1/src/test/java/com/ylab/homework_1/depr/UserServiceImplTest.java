//package com.ylab.homework_1;
//
//import com.ylab.homework_1.infrastructure.service.UserServiceImpl;
//import com.ylab.homework_1.common.Role;
//import com.ylab.homework_1.domain.model.User;
//import com.ylab.homework_1.domain.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        user = new User("John Doe", "john@example.com", "password123", Role.USER);
//    }
//
//    @Test
//    void shouldRegisterUserSuccessfully() {
//        Mockito.doNothing().when(userRepository).create(user);
//        userService.register(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
//        Mockito.verify(userRepository, Mockito.times(1)).create(user);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenEmailIsNullOnRegister() {
//        Assertions.assertThatThrownBy(() -> userService.register("John", null, "password", Role.USER))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("email is required");
//    }
//
//    @Test
//    void shouldLoginSuccessfully() {
//        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
//        Assertions.assertThat(loggedInUser).isEqualTo(user);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenInvalidCredentials() {
//        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
//        Assertions.assertThatThrownBy(() -> userService.login(user.getEmail(), "wrongPassword"))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Invalid email or password");
//    }
//
//    @Test
//    void shouldDeleteUserByEmail() {
//        Mockito.doNothing().when(userRepository).delete(user.getEmail());
//        userService.delete(user.getEmail());
//        Mockito.verify(userRepository, Mockito.times(1)).delete(user.getEmail());
//    }
//
//    @Test
//    void shouldFindUserByEmail() {
//        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        User foundUser = userService.findByEmail(user.getEmail());
//        Assertions.assertThat(foundUser).isEqualTo(user);
//    }
//
//    @Test
//    void shouldThrowExceptionIfUserNotFound() {
//        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
//        Assertions.assertThatThrownBy(() -> userService.findByEmail(user.getEmail()))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Email not found");
//    }
//}

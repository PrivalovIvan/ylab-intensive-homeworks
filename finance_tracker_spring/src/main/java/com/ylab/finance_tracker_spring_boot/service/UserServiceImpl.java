package com.ylab.finance_tracker_spring_boot.service;

import com.ylab.finance_tracker_spring_boot.common.Role;
import com.ylab.finance_tracker_spring_boot.domain.model.User;
import com.ylab.finance_tracker_spring_boot.domain.repository.UserRepository;
import com.ylab.finance_tracker_spring_boot.domain.service.UserService;
import com.ylab.finance_tracker_spring_boot.dto.UserDTO;
import com.ylab.finance_tracker_spring_boot.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public void register(UserDTO userDTO) throws SQLException, IllegalArgumentException {
        if (userRepository.getByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        String passwordEncode = passwordEncoder.encode(userDTO.getPassword());
        System.out.println("passwordEncode: " + passwordEncode);
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncode);
        user.setRole(Role.USER);
        System.out.println("user2: " + user);
        userRepository.save(user);
    }

    @Override
    public UserDTO login(String email, String password) throws IllegalArgumentException, SQLException {
        User user = userRepository.getByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email: " + email + " not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID id, String newName, String newEmail, String newPassword) throws SQLException {
        User user = userRepository.getById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (newEmail != null && userRepository.getByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        String name = newName != null ? newName : user.getName();
        String email = newEmail != null ? newEmail : user.getEmail();
        String password = newPassword != null ? newPassword : user.getPassword();

        UserDTO updatedUser = new UserDTO(id, name, email, password, user.getRole());
        userRepository.update(userMapper.toUser(updatedUser));
        return updatedUser;
    }

    @Override
    public void delete(String email) throws SQLException {
        userRepository.delete(email);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) throws SQLException, IllegalArgumentException {
        User user = userRepository.getByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .role(user.getRole())
                .uuid(user.getUuid())
                .build();
        return Optional.ofNullable(userDTO);
    }

    @Override
    public Optional<UserDTO> findByUuid(UUID id) throws SQLException {
        return userRepository.getById(id).map(userMapper::toUserDTO);
    }

    @Override
    public List<UserDTO> findAll() throws SQLException {
        return userRepository.getAll().stream().map(userMapper::toUserDTO).toList();
    }
}

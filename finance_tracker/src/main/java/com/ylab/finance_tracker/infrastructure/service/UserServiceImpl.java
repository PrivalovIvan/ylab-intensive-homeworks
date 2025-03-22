package com.ylab.finance_tracker.infrastructure.service;

import com.ylab.finance_tracker.domain.model.User;
import com.ylab.finance_tracker.domain.repository.UserRepository;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.mapper.UserMapper;
import com.ylab.finance_tracker.usecase.service.PasswordEncoder;
import com.ylab.finance_tracker.usecase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new PasswordEncoderImpl();
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public void register(UserDTO userDTO) throws SQLException, IllegalArgumentException {
        User user = userRepository.getByEmail(userDTO.getEmail()).orElse(null);
        if (user == null) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(userMapper.toUser(userDTO));
        } else {
            throw new IllegalArgumentException("User already exists");
        }
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
    public UserDTO findByEmail(String email) throws SQLException {
        return userMapper.toUserDTO(userRepository.getByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Email: " + email + " not found")));
    }

    @Override
    public UserDTO findByUuid(UUID id) throws SQLException {
        return userMapper.toUserDTO(userRepository.getById(id).orElseThrow(() ->
                new IllegalArgumentException("ID: " + id + " not found")));
    }

    @Override
    public List<UserDTO> findAll() throws SQLException {
        return userRepository.getAll().stream().map(userMapper::toUserDTO).toList();
    }
}

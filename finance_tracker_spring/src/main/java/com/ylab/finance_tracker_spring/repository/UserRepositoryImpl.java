package com.ylab.finance_tracker_spring.repository;

import com.ylab.finance_tracker_spring.common.Role;
import com.ylab.finance_tracker_spring.domain.model.User;
import com.ylab.finance_tracker_spring.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final static String INSERT_USER_SQL = "INSERT INTO finance.users(name, email, password, role) VALUES (?,?,?,?)";
    private final static String UPDATE_USER_SQL = "UPDATE finance.users SET name = ?, email = ?, password = ? WHERE id = ?";
    private final static String SELECT_USER_BY_EMAIL_SQL = "SELECT * FROM finance.users WHERE email = ?";
    private final static String SELECT_USER_BY_ID_SQL = "SELECT * FROM finance.users WHERE id = ?";
    private final static String DELETE_USER_SQL = "DELETE FROM finance.users WHERE email = ?";
    private final static String SELECT_ALL_USER_SQL = "SELECT * FROM finance.users";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(User user) throws SQLException {
        jdbcTemplate.update(INSERT_USER_SQL, user.getName(), user.getEmail(), user.getPassword(), user.getRole().name());
    }

    @Override
    public void update(User user) throws SQLException {
        jdbcTemplate.update(UPDATE_USER_SQL, user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return jdbcTemplate.query(SELECT_USER_BY_EMAIL_SQL, this::resultSetToUser, email).stream().findFirst();
    }

    @Override
    public Optional<User> getById(UUID id) {
        return jdbcTemplate.query(SELECT_USER_BY_ID_SQL, this::resultSetToUser, id).stream().findFirst();
    }

    @Override
    public void delete(String email) throws SQLException {
        jdbcTemplate.update(DELETE_USER_SQL, email);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(SELECT_ALL_USER_SQL, this::resultSetToUser);
    }

    private User resultSetToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .uuid(resultSet.getObject("id", UUID.class))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .role(Role.valueOf(resultSet.getString("role"))).build();
    }
}

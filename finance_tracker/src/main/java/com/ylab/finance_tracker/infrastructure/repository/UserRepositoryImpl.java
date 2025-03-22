package com.ylab.finance_tracker.infrastructure.repository;

import com.ylab.finance_tracker.common.Role;
import com.ylab.finance_tracker.domain.model.User;
import com.ylab.finance_tracker.domain.repository.UserRepository;
import com.ylab.finance_tracker.infrastructure.datasource.PostgresDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public void save(User user) throws SQLException {
        String insertUserSQL = "INSERT INTO finance.users(name, email, password, role) VALUES (?,?,?,?)";
        try (Connection connection = PostgresDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole().toString());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(User user) throws SQLException {
        String updateUserSQL = "UPDATE finance.users SET name = ?, email = ?, password = ? WHERE id = ?";
        try (Connection connection = PostgresDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateUserSQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setObject(4, user.getUuid());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Optional<User> getByEmail(String email) throws SQLException {
        String selectUserByEmailSQL = "SELECT * FROM finance.users WHERE email = ?";
        try (Connection connection = PostgresDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectUserByEmailSQL);
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToUser(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getById(UUID id) throws SQLException {
        String selectUserByEmailSQL = "SELECT * FROM finance.users WHERE id = ?";
        try (Connection connection = PostgresDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectUserByEmailSQL);
            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToUser(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(String email) throws SQLException {
        String deleteUserSQL = "DELETE FROM finance.users WHERE email = ?";
        try (Connection connection = PostgresDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteUserSQL);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        }

    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String selectAllUsersSQL = "SELECT * FROM finance.users";
        try (Connection connection = PostgresDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectAllUsersSQL);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = resultSetToUser(resultSet);
                    users.add(user);
                }
            }
        }
        return users;
    }

    private static User resultSetToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role")));
    }
}

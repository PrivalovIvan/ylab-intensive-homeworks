package com.ylab.homework_1.infrastructure.repository;

import com.ylab.homework_1.domain.model.Goal;
import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.usecase.repository.GoalRepository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoalRepositoryImpl implements GoalRepository {

    @Override
    public void save(Goal goal) throws SQLException {
        String insertGoalSQL = "INSERT INTO finance.goals (email, title, target_amount, saved_amount) VALUES (?, ?, ?, ?)";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertGoalSQL)) {
            statement.setString(1, goal.getEmail());
            statement.setString(2, goal.getTitle());
            statement.setBigDecimal(3, goal.getTargetAmount());
            statement.setBigDecimal(4, goal.getSavedAmount());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Goal goal) throws SQLException {
        String updateGoalSQL = "UPDATE finance.goals SET title = ?, target_amount = ?, saved_amount = ? WHERE email = ? AND title = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateGoalSQL)) {
            statement.setString(1, goal.getTitle());
            statement.setBigDecimal(2, goal.getTargetAmount());
            statement.setBigDecimal(3, goal.getSavedAmount());
            statement.setString(4, goal.getEmail());
            statement.setString(5, goal.getTitle());
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Goal> findByName(String email, String name) throws SQLException {
        String findByNameSQL = "SELECT * FROM finance.goals WHERE email = ? AND title = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(findByNameSQL)) {
            statement.setString(1, email);
            statement.setString(2, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSetToGoal(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Goal> findAllByUser(String email) throws SQLException {
        String findAllByUserSQL = "SELECT * FROM finance.goals WHERE email = ?";
        List<Goal> goals = new ArrayList<>();
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(findAllByUserSQL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                goals.add(resultSetToGoal(resultSet));
            }
        }
        return goals;
    }

    @Override
    public void delete(String email, String name) throws SQLException {
        String deleteGoalSQL = "DELETE FROM finance.goals WHERE email = ? AND title = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteGoalSQL)) {
            statement.setString(1, email);
            statement.setString(2, name);
            statement.executeUpdate();
        }
    }

    private Goal resultSetToGoal(ResultSet resultSet) throws SQLException {
        return new Goal(
                resultSet.getString("email"),
                resultSet.getString("title"),
                resultSet.getBigDecimal("target_amount"),
                resultSet.getBigDecimal("saved_amount")
        );
    }
}
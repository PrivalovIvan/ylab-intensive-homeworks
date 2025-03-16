package com.ylab.homework_1.infrastructure.repository;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.usecase.repository.TransactionRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionRepositoryImpl implements TransactionRepository {
    @Override
    public void save(Transaction transaction) throws SQLException {
        String insertTransactionSQL = "INSERT INTO finance.transactions (uuid, email, type, amount, category, name_goal, date, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertTransactionSQL)) {
            preparedStatement.setObject(1, UUID.randomUUID()); // Генерируем UUID
            preparedStatement.setString(2, transaction.getEmail());
            preparedStatement.setString(3, transaction.getType().toString());
            preparedStatement.setBigDecimal(4, transaction.getAmount());
            preparedStatement.setString(5, transaction.getCategory());
            preparedStatement.setString(6, transaction.getNameGoal());
            preparedStatement.setDate(7, java.sql.Date.valueOf(transaction.getDate()));
            preparedStatement.setString(8, transaction.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(Transaction transaction) throws SQLException {
        String updateTransactionSQL = "UPDATE finance.transactions SET amount = ?, category = ?, description = ? WHERE id = ? AND email = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateTransactionSQL)) {
            preparedStatement.setBigDecimal(1, transaction.getAmount());
            preparedStatement.setString(2, transaction.getCategory());
            preparedStatement.setString(3, transaction.getDescription());
            preparedStatement.setLong(4, transaction.getId());
            preparedStatement.setString(5, transaction.getEmail());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Transaction with id " + transaction.getId() + " not found or email mismatch");
            }
        }
    }

    @Override
    public List<Transaction> getTransactionsByUserEmail(String email) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String getTransactionSQL = "SELECT * FROM finance.transactions WHERE email = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getTransactionSQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(resultSetToTransaction(resultSet));
                }
            }
        }
        return transactions;
    }

    @Override
    public Optional<Transaction> getById(Long id) throws SQLException {
        String selectTransactionSQL = "SELECT * FROM finance.transactions WHERE id = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectTransactionSQL)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToTransaction(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(String email, Long id) throws SQLException {
        String deleteTransactionSQL = "DELETE FROM finance.transactions WHERE email = ? AND id = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteTransactionSQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterDate(String email, LocalDate date) throws SQLException {
        String getTransactionSQL = "SELECT * FROM finance.transactions WHERE email = ? AND date = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getTransactionSQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(resultSetToTransaction(resultSet));
                }
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterCategory(String email, String category) throws SQLException {
        String getTransactionSQL = "SELECT * FROM finance.transactions WHERE email = ? AND category = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getTransactionSQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, category);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(resultSetToTransaction(resultSet));
                }
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterType(String email, TransactionType type) throws SQLException {
        String getTransactionSQL = "SELECT * FROM finance.transactions WHERE email = ? AND type = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getTransactionSQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, type.toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(resultSetToTransaction(resultSet));
                }
            }
        }
        return transactions;
    }

    private Transaction resultSetToTransaction(ResultSet resultSet) throws SQLException {
        return new Transaction(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                TransactionType.valueOf(resultSet.getString("type")),
                resultSet.getBigDecimal("amount"),
                resultSet.getString("category"),
                resultSet.getString("name_goal"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getString("description")
        );
    }
}
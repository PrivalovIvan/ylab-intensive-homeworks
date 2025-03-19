package com.ylab.homework_1.infrastructure.repository;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.domain.repository.TransactionRepository;

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
        String insertTransactionSQL =
                "INSERT INTO finance.transactions (email_user, type, amount, category, name_goal, date, description ) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertTransactionSQL)) {
            preparedStatement.setString(1, transaction.getEmail());
            preparedStatement.setString(2, transaction.getType().toString());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getCategory());
            preparedStatement.setString(5, transaction.getNameGoal());
            preparedStatement.setDate(6, java.sql.Date.valueOf(transaction.getDate()));
            preparedStatement.setString(7, transaction.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(Transaction transaction) throws SQLException {
        String updateTransactionSQL = "UPDATE finance.transactions SET amount = ?, category = ?, description = ? WHERE id = ? AND email_user = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateTransactionSQL)) {
            preparedStatement.setBigDecimal(1, transaction.getAmount());
            preparedStatement.setString(2, transaction.getCategory());
            preparedStatement.setString(3, transaction.getDescription());
            preparedStatement.setObject(4, transaction.getUuid());
            preparedStatement.setString(5, transaction.getEmail());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Transaction with id " + transaction.getUuid() + " not found or email mismatch");
            }
        }
    }

    @Override
    public List<Transaction> getTransactionsByUserEmail(String email) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String getTransactionSQL = "SELECT * FROM finance.transactions WHERE email_user = ?";
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
    public Optional<Transaction> getById(UUID id) throws SQLException {
        String selectTransactionSQL = "SELECT * FROM finance.transactions WHERE id = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectTransactionSQL)) {
            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToTransaction(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(String email, UUID id) throws SQLException {
        String deleteTransactionSQL = "DELETE FROM finance.transactions WHERE email_user = ? AND id = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteTransactionSQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setObject(2, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterDate(String email, LocalDate date) throws SQLException {
        String getTransactionSQL = "SELECT * FROM finance.transactions WHERE email_user = ? AND date = ?";
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
        String getTransactionSQL = "SELECT * FROM finance.transactions WHERE email_user = ? AND category = ?";
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
        String getTransactionSQL = "SELECT * FROM finance.transactions WHERE email_user = ? AND type = ?";
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
        return Transaction.builder()
                .uuid(resultSet.getObject(1, UUID.class))
                .email(resultSet.getString("email_user"))
                .type(TransactionType.valueOf(resultSet.getString("type")))
                .amount(resultSet.getBigDecimal("amount"))
                .category(resultSet.getString("category"))
                .nameGoal(resultSet.getString("name_goal"))
                .date(resultSet.getDate("date").toLocalDate())
                .description(resultSet.getString("description"))
                .build();
    }
}
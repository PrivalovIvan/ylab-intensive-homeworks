package com.ylab.finance_tracker.infrastructure.repository;

import com.ylab.finance_tracker.domain.model.Budget;
import com.ylab.finance_tracker.domain.repository.BudgetRepository;
import com.ylab.finance_tracker.infrastructure.datasource.PostgresDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

public class BudgetRepositoryImpl implements BudgetRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public void save(Budget budget) throws SQLException {
        String insertBudgetSQL = "INSERT INTO finance.budgets (email_user, year_month, budget, spent) VALUES (?, ?, ?, ?)";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertBudgetSQL)) {
            preparedStatement.setString(1, budget.getEmail());
            preparedStatement.setString(2, budget.getYearMonth().format(FORMATTER));
            preparedStatement.setBigDecimal(3, budget.getBudget());
            preparedStatement.setBigDecimal(4, budget.getSpent());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(Budget budget) throws SQLException {
        String updateBudgetSQL = "UPDATE finance.budgets SET spent = ? WHERE email_user = ? AND year_month = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateBudgetSQL)) {
            preparedStatement.setBigDecimal(1, budget.getSpent());
            preparedStatement.setString(2, budget.getEmail());
            preparedStatement.setString(3, budget.getYearMonth().format(FORMATTER));
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Optional<Budget> findByUserAndMonth(String email, YearMonth yearMonth) throws SQLException {
        String getUserAndMonth = "SELECT * FROM finance.budgets WHERE email_user = ? AND year_month = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getUserAndMonth)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, yearMonth.format(FORMATTER));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(setResultToBudget(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(String email, YearMonth month) throws SQLException {
        String deleteBudgetSQL = "DELETE FROM finance.budgets WHERE email_user = ? AND year_month = ?";
        try (var connection = PostgresDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteBudgetSQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, month.format(FORMATTER));
            preparedStatement.executeUpdate();
        }
    }

    private Budget setResultToBudget(ResultSet resultSet) throws SQLException {
        return Budget.builder()
                .uuid(resultSet.getObject(1, UUID.class))
                .email(resultSet.getString("email_user"))
                .yearMonth(YearMonth.parse(resultSet.getString("year_month"), FORMATTER))
                .budget(resultSet.getBigDecimal("budget"))
                .spent(resultSet.getBigDecimal("spent"))
                .build();

    }
}
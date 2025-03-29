package com.ylab.finance_tracker_spring.repository;

import com.ylab.finance_tracker_spring.common.TransactionType;
import com.ylab.finance_tracker_spring.domain.model.Transaction;
import com.ylab.finance_tracker_spring.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {
    private final static String INSERT_TRANSACTION_SQL =
            "INSERT INTO finance.transactions (email_user, type, amount, category, date, description ) VALUES (?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_TRANSACTION_SQL =
            "UPDATE finance.transactions SET amount = ?, category = ?, description = ? WHERE id = ? AND email_user = ?";
    private final static String GET_TRANSACTION_SQL = "SELECT * FROM finance.transactions WHERE email_user = ?";
    private final static String SELECT_TRANSACTION_SQL = "SELECT * FROM finance.transactions WHERE id = ?";
    private final static String DELETE_TRANSACTION_SQL = "DELETE FROM finance.transactions WHERE email_user = ? AND id = ?";
    private final static String GET_TRANSACTION_FILTER_DATE_SQL = "SELECT * FROM finance.transactions WHERE email_user = ? AND date = ?";
    private final static String GET_TRANSACTION_FILTER_CATEGORY_SQL = "SELECT * FROM finance.transactions WHERE email_user = ? AND category = ?";
    private final static String GET_TRANSACTION_FILTER_TYPE_SQL = "SELECT * FROM finance.transactions WHERE email_user = ? AND type = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Transaction transaction) throws SQLException {
        jdbcTemplate.update(INSERT_TRANSACTION_SQL,
                transaction.getEmail(),
                transaction.getType().toString(),
                transaction.getAmount(),
                transaction.getCategory(),
                java.sql.Date.valueOf(transaction.getDate()),
                transaction.getDescription());
    }

    @Override
    public void update(Transaction transaction) throws SQLException {
        jdbcTemplate.update(UPDATE_TRANSACTION_SQL, transaction.getAmount(), transaction.getCategory(), transaction.getDescription(),
                transaction.getUuid(), transaction.getEmail());
    }

    @Override
    public List<Transaction> getTransactionsByUserEmail(String email) {
        return jdbcTemplate.query(GET_TRANSACTION_SQL, this::resultSetToTransaction, email);
    }

    @Override
    public Optional<Transaction> getById(UUID id) {
        return jdbcTemplate.query(SELECT_TRANSACTION_SQL, this::resultSetToTransaction, id).stream().findFirst();
    }

    @Override
    public void delete(String email, UUID id) throws SQLException {
        jdbcTemplate.update(DELETE_TRANSACTION_SQL, email, id);
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterDate(String email, LocalDate date) {
        return jdbcTemplate.query(GET_TRANSACTION_FILTER_DATE_SQL, this::resultSetToTransaction, email, date);
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterCategory(String email, String category) {
        return jdbcTemplate.query(GET_TRANSACTION_FILTER_CATEGORY_SQL, this::resultSetToTransaction, email, category);
    }

    @Override
    public List<Transaction> getTransactionsByUserEmailFilterType(String email, TransactionType type) {
        return jdbcTemplate.query(GET_TRANSACTION_FILTER_TYPE_SQL, this::resultSetToTransaction, email, type);
    }

    private Transaction resultSetToTransaction(ResultSet resultSet, int rowNum) throws SQLException {
        return Transaction.builder()
                .uuid(resultSet.getObject(1, UUID.class))
                .email(resultSet.getString("email_user"))
                .type(TransactionType.valueOf(resultSet.getString("type")))
                .amount(resultSet.getBigDecimal("amount"))
                .category(resultSet.getString("category"))
                .date(resultSet.getDate("date").toLocalDate())
                .description(resultSet.getString("description"))
                .build();
    }
}
package com.ylab.auditing.repository;

import com.ylab.auditing.model.Action;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserActionAuditRepositoryImpl implements UserActionAuditRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Action action) {
        String INSERT_ACTION_SQL = "INSERT INTO audit.user_action (user_email, action, action_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(INSERT_ACTION_SQL, x -> {
            x.setString(1, action.userEmail());
            x.setString(2, action.action());
            x.setTimestamp(3, Timestamp.from(action.actionDate()));
        });
    }

    @Override
    public List<Action> findAllActionUserEmail(String email, int limit) {
        String SELECT_ALL_ACTION_USER_EMAIL_SQL = "SELECT * FROM audit.user_action WHERE user_email = ? ORDER BY action_date DESC LIMIT ?";
        return jdbcTemplate.query(SELECT_ALL_ACTION_USER_EMAIL_SQL, this::resultSetToAction, email, limit);
    }

    public Action resultSetToAction(ResultSet resultSet, int rowNum) throws SQLException {
        return Action.builder()
                .uuid(resultSet.getObject("id", UUID.class))
                .userEmail(resultSet.getString("user_email"))
                .action(resultSet.getString("action"))
                .actionDate(resultSet.getTimestamp("action_date").toInstant()).build();
    }
}
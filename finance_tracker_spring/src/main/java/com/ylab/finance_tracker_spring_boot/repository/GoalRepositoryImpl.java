package com.ylab.finance_tracker_spring_boot.repository;

import com.ylab.finance_tracker_spring_boot.domain.model.Goal;
import com.ylab.finance_tracker_spring_boot.domain.repository.GoalRepository;
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
public class GoalRepositoryImpl implements GoalRepository {
    private final static String INSERT_GOAL_SQL = "INSERT INTO finance.goals (email_user, title, target_amount, saved_amount) VALUES (?, ?, ?, ?)";
    private final static String UPDATE_GOAL_SQL = "UPDATE finance.goals SET title = ?, target_amount = ?, saved_amount = ? WHERE email_user = ? AND title = ?";
    private final static String FIND_BY_NAME_SQL = "SELECT * FROM finance.goals WHERE email_user = ? AND title = ?";
    private final static String FIND_ALL_BY_USER_SQL = "SELECT * FROM finance.goals WHERE email_user = ?";
    private final static String DELETE_GOAL_SQL = "DELETE FROM finance.goals WHERE email_user = ? AND title = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Goal goal) throws SQLException {
        jdbcTemplate.update(INSERT_GOAL_SQL,
                goal.getEmail(),
                goal.getTitle(),
                goal.getTargetAmount(),
                goal.getSavedAmount());
    }

    @Override
    public void update(Goal goal) throws SQLException {
        jdbcTemplate.update(UPDATE_GOAL_SQL,
                goal.getTitle(),
                goal.getTargetAmount(),
                goal.getSavedAmount(),
                goal.getEmail(),
                goal.getTitle());

    }

    @Override
    public Optional<Goal> findByName(String email, String name) {
        return jdbcTemplate.query(FIND_BY_NAME_SQL, this::resultSetToGoal, email, name).stream().findFirst();
    }

    @Override
    public List<Goal> findAllByUser(String email) {
        return jdbcTemplate.query(FIND_ALL_BY_USER_SQL, this::resultSetToGoal, email);
    }

    @Override
    public void delete(String email, String name) throws SQLException {
        jdbcTemplate.update(DELETE_GOAL_SQL, email, name);
    }

    private Goal resultSetToGoal(ResultSet resultSet, int numRow) throws SQLException {
        return Goal.builder()
                .uuid(resultSet.getObject(1, UUID.class))
                .email(resultSet.getString("email_user"))
                .title(resultSet.getString("title"))
                .targetAmount(resultSet.getBigDecimal("target_amount"))
                .savedAmount(resultSet.getBigDecimal("saved_amount"))
                .build();
    }
}
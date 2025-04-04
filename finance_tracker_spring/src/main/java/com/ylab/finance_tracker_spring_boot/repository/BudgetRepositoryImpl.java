package com.ylab.finance_tracker_spring_boot.repository;

import com.ylab.finance_tracker_spring_boot.domain.model.Budget;
import com.ylab.finance_tracker_spring_boot.domain.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    private final static String INSERT_BUDGET_SQL =
            "INSERT INTO finance.budgets (email_user, year_month, budget, spent) VALUES (?, ?, ?, ?)";

    private final static String UPDATE_BUDGET_SQL =
            "UPDATE finance.budgets SET spent = ? WHERE email_user = ? AND year_month = ?";

    private final static String GET_USER_AND_MONTH_SQL =
            "SELECT * FROM finance.budgets WHERE email_user = ? AND year_month = ?";

    private final static String DELETE_BUDGET_SQL =
            "DELETE FROM finance.budgets WHERE email_user = ? AND year_month = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Budget budget) throws SQLException {
        jdbcTemplate.update(
                INSERT_BUDGET_SQL,
                budget.getEmail(),
                budget.getYearMonth().format(FORMATTER),
                budget.getBudget(),
                budget.getSpent());
    }

    @Override
    public void update(Budget budget) throws SQLException {
        jdbcTemplate.update(UPDATE_BUDGET_SQL, budget.getSpent(), budget.getEmail(), budget.getYearMonth().format(FORMATTER));
    }

    @Override
    public Optional<Budget> findByUserAndMonth(String email, YearMonth yearMonth) {
        return jdbcTemplate.query(GET_USER_AND_MONTH_SQL, this::setResultToBudget, email, yearMonth.format(FORMATTER))
                .stream().findFirst();
    }

    @Override
    public void delete(String email, YearMonth month) throws SQLException {
        jdbcTemplate.update(DELETE_BUDGET_SQL, email, month.format(FORMATTER));
    }

    private Budget setResultToBudget(ResultSet resultSet, int rowNum) throws SQLException {
        return Budget.builder()
                .uuid(resultSet.getObject(1, UUID.class))
                .email(resultSet.getString("email_user"))
                .yearMonth(YearMonth.parse(resultSet.getString("year_month"), FORMATTER))
                .budget(resultSet.getBigDecimal("budget"))
                .spent(resultSet.getBigDecimal("spent"))
                .build();

    }
}
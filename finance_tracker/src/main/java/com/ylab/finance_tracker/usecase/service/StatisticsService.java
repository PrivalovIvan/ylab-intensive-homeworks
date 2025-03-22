package com.ylab.finance_tracker.usecase.service;

import com.ylab.finance_tracker.common.TransactionType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public interface StatisticsService {
    BigDecimal getCurrentBalance(String email) throws SQLException;

    BigDecimal getTotal(String email, LocalDate from, LocalDate to, TransactionType type) throws SQLException;

    Map<String, BigDecimal> getExpensesByCategory(String email, LocalDate from, LocalDate to) throws SQLException;

    String generateFinancialReport(String email, LocalDate from, LocalDate to) throws SQLException;
}

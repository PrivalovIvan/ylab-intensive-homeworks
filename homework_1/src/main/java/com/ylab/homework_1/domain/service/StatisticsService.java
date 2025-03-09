package com.ylab.homework_1.domain.service;

import com.ylab.homework_1.common.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface StatisticsService {
    BigDecimal getCurrentBalance(String email);

    BigDecimal getTotal(String email, LocalDate from, LocalDate to, TransactionType type);

//    BigDecimal getTotalExpenses(String email, LocalDate from, LocalDate to);

    Map<String, BigDecimal> getExpensesByCategory(String email, LocalDate from, LocalDate to);

    String generateFinancialReport(String email, LocalDate from, LocalDate to);
}

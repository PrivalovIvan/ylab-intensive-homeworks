package com.ylab.finance_tracker.infrastructure.service;

import com.ylab.finance_tracker.common.TransactionType;
import com.ylab.finance_tracker.usecase.dto.TransactionDTO;
import com.ylab.finance_tracker.usecase.service.StatisticsService;
import com.ylab.finance_tracker.usecase.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final TransactionService transactionService;

    @Override
    public BigDecimal getCurrentBalance(String email) throws SQLException {
        List<TransactionDTO> transactions = transactionService.findAllTransactionUser(email);
        return transactions.stream()
                .map(t -> t.getType() == TransactionType.INCOME ? t.getAmount() : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotal(String email, LocalDate from, LocalDate to, TransactionType type) throws SQLException {
        return transactionService.findAllTransactionUser(email).stream()
                .filter(t -> t.getType() == type)
                .filter(t -> isWithinRange(t.getDate(), from, to))
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<String, BigDecimal> getExpensesByCategory(String email, LocalDate from, LocalDate to) throws SQLException {
        return transactionService.findAllTransactionUser(email).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> isWithinRange(t.getDate(), from, to))
                .collect(Collectors.groupingBy(
                        TransactionDTO::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, TransactionDTO::getAmount, BigDecimal::add)));
    }

    @Override
    public String generateFinancialReport(String email, LocalDate from, LocalDate to) throws SQLException {
        BigDecimal income = getTotal(email, from, to, TransactionType.INCOME);
        BigDecimal expense = getTotal(email, from, to, TransactionType.EXPENSE);
        BigDecimal balance = getCurrentBalance(email);
        Map<String, BigDecimal> expensesByCategory = getExpensesByCategory(email, from, to);
        StringBuilder report = new StringBuilder();
        report.append("Balance: ").append(balance).append("\n");
        report.append("Income: ").append(income).append("\n");
        report.append("Expense: ").append(expense).append("\n");
        report.append("Category: \n");

        for (Map.Entry<String, BigDecimal> entry : expensesByCategory.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return report.toString();
    }

    private boolean isWithinRange(LocalDate date, LocalDate from, LocalDate to) {
        return (date.isEqual(from) || date.isAfter(from)) && (date.isEqual(to) || date.isBefore(to));
    }
}
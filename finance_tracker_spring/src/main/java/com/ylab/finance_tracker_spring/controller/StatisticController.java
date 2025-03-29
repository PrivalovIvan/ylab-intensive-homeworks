package com.ylab.finance_tracker_spring.controller;

import com.ylab.finance_tracker_spring.annotation.Loggable;
import com.ylab.finance_tracker_spring.common.TransactionType;
import com.ylab.finance_tracker_spring.domain.service.StatisticsService;
import com.ylab.finance_tracker_spring.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@Loggable
@RequestMapping("/statistics")
public class StatisticController {
    private final StatisticsService statisticsService;
    private final AuthService authService;

    @GetMapping("/balance")
    public ResponseEntity<Map<String, BigDecimal>> GetCurrentBalanceStatistics() throws SQLException {
        BigDecimal balance = statisticsService.getCurrentBalance(authService.getCurrentUser().getEmail());
        Map<String, BigDecimal> balanceOut = Map.of("balance", balance);
        return ResponseEntity.ok(balanceOut);
    }

    @GetMapping("/expense")
    public ResponseEntity<Map<String, BigDecimal>> GetExpenseForThePeriodServlet(@RequestParam LocalDate from, @RequestParam LocalDate to) throws SQLException {
        BigDecimal result = statisticsService.getTotal(authService.getCurrentUser().getEmail(), from, to, TransactionType.EXPENSE);
        Map<String, BigDecimal> resultOut = Map.of("result", result);
        return ResponseEntity.ok(resultOut);
    }

    @GetMapping("/income")
    public ResponseEntity<Map<String, BigDecimal>> GetIncomeForThePeriodServlet(@RequestParam LocalDate from, @RequestParam LocalDate to) throws SQLException {
        BigDecimal result = statisticsService.getTotal(authService.getCurrentUser().getEmail(), from, to, TransactionType.INCOME);
        Map<String, BigDecimal> resultOut = Map.of("result", result);
        return ResponseEntity.ok(resultOut);
    }

    @GetMapping("/analysis")
    public ResponseEntity<Map<String, BigDecimal>> GetCostAnalysisStatistics(@RequestParam LocalDate from, @RequestParam LocalDate to) throws SQLException {
        return ResponseEntity.ok(statisticsService.getExpensesByCategory(authService.getCurrentUser().getEmail(), from, to));
    }

    @GetMapping("/report")
    public ResponseEntity<String> GetReportStatistics(@RequestParam LocalDate from, @RequestParam LocalDate to) throws SQLException {
        return ResponseEntity.ok(statisticsService.generateFinancialReport(authService.getCurrentUser().getEmail(), from, to));
    }

}

package com.ylab.finance_tracker_spring_boot.controller;

import com.ylab.finance_tracker_spring_boot.common.TransactionType;
import com.ylab.finance_tracker_spring_boot.domain.service.StatisticsService;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import com.ylab.finance_tracker_spring_boot.annotation.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Statistic Controller", description = "API для получения финансовой статистики")
public class StatisticController {
    private final StatisticsService statisticsService;
    private final AuthService authService;


    @Operation(
            summary = "Получение баланса",
            description = "Получение баланса пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Баланс получен"
                    )
            }
    )
    @GetMapping("/balance")
    public ResponseEntity<Map<String, BigDecimal>> GetCurrentBalanceStatistics() throws SQLException {
        BigDecimal balance = statisticsService.getCurrentBalance(authService.getCurrentUser().getEmail());
        Map<String, BigDecimal> balanceOut = Map.of("balance", balance);
        return ResponseEntity.ok(balanceOut);
    }

    @Operation(
            summary = "Получение статистики расходов",
            description = "Получение статистики расходов за указанный период времени",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет получен"
                    )
            }
    )
    @GetMapping("/expense")
    public ResponseEntity<Map<String, BigDecimal>> GetExpenseForThePeriodServlet(
            @Parameter(
                    description = "Начальная дата",
                    required = true
            )
            @RequestParam LocalDate from,
            @Parameter(
                    description = "Конечная дата",
                    required = true
            )
            @RequestParam LocalDate to)
            throws SQLException {
        BigDecimal result = statisticsService.getTotal(authService.getCurrentUser().getEmail(), from, to, TransactionType.EXPENSE);
        Map<String, BigDecimal> resultOut = Map.of("result", result);
        return ResponseEntity.ok(resultOut);
    }


    @Operation(
            summary = "Получение статистики доходов",
            description = "Получение статистики доходов за указанный период времени",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет получен"
                    )
            }
    )
    @GetMapping("/income")
    public ResponseEntity<Map<String, BigDecimal>> GetIncomeForThePeriodServlet(
            @Parameter(
                    description = "Начальная дата",
                    required = true
            )
            @RequestParam LocalDate from,
            @Parameter(
                    description = "Конечная дата",
                    required = true
            )
            @RequestParam LocalDate to)
            throws SQLException {
        BigDecimal result = statisticsService.getTotal(authService.getCurrentUser().getEmail(), from, to, TransactionType.INCOME);
        Map<String, BigDecimal> resultOut = Map.of("result", result);
        return ResponseEntity.ok(resultOut);
    }

    @Operation(
            summary = "Получить отчет",
            description = "Получить отчет по конкретной категории",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет получен"
                    )
            }
    )
    @GetMapping("/analysis")
    public ResponseEntity<Map<String, BigDecimal>> GetCostAnalysisStatistics(
            @Parameter(
                    description = "Начальная дата",
                    required = true
            )
            @RequestParam LocalDate from,
            @Parameter(
                    description = "Конечная дата",
                    required = true
            )
            @RequestParam LocalDate to)
            throws SQLException {
        return ResponseEntity.ok(statisticsService.getExpensesByCategory(authService.getCurrentUser().getEmail(), from, to));
    }

    @Operation(
            summary = "Получить общий отчет",
            description = "Получение отчета по всем пунктам за определенный отрезок времени"
    )
    @GetMapping("/report")
    public ResponseEntity<String> GetReportStatistics(
            @Parameter(
                    description = "Начальная дата",
                    required = true
            )
            @RequestParam LocalDate from,
            @Parameter(
                    description = "Конечная дата",
                    required = true
            )
            @RequestParam LocalDate to) throws SQLException {
        return ResponseEntity.ok(statisticsService.generateFinancialReport(authService.getCurrentUser().getEmail(), from, to));
    }

}

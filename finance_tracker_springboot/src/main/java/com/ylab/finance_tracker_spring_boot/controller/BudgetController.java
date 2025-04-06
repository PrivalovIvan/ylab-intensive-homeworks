package com.ylab.finance_tracker_spring_boot.controller;

import com.ylab.finance_tracker_spring_boot.domain.service.BudgetService;
import com.ylab.finance_tracker_spring_boot.dto.BudgetDTO;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import com.ylab.logging.annotation.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
@Validated
@Loggable
@Tag(name = "Budget Controller", description = "API для работы с бюджетом")
public class BudgetController {
    private final BudgetService budgetService;
    private final AuthService authService;

    @Operation(
            summary = "Получение бюджета",
            description = "Получение бюджета на определенный месяц",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Бюджет получен",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Бюджет за указанный месяц не найден"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<BudgetDTO> getBudgets(
            @Parameter(
                    description = "Месяц, за который нужно получить бюджет",
                    required = true
            )
            @RequestParam YearMonth yearMonth)
            throws SQLException {
        var user = authService.getCurrentUser();
        var budget = budgetService.getBudget(user.getEmail(), yearMonth);
        return budget.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Создание бюджета",
            description = "Создать бюджет на указанный месяц",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Бюджет создан",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetDTO.class)
                            )
                    )
            }
    )
    @PostMapping("create")
    public ResponseEntity<BudgetDTO> createBudget(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Получение данных для создания бюджета",
                    required = true
            )
            @Valid @RequestBody BudgetDTO budgetDTO)
            throws SQLException {
        budgetService.createBudget(budgetDTO);
        return ResponseEntity.ok(budgetDTO);
    }

    @Operation(
            summary = "Обновить бюджет",
            description = "Добавить расход в бюджет за указанный месяц",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Бюджет успешно обновлен",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Бюджет не найден за указанный месяц"
                    )
            }
    )
    @PutMapping("/update")
    public ResponseEntity<BudgetDTO> updateBudget(
            @Parameter(
                    description = "Месяц, в который будет добавлен расход",
                    required = true
            )
            @RequestParam YearMonth yearMonth,
            @Parameter(
                    description = "Сумма, которая будет указана в качестве расхода",
                    required = true
            )
            @RequestParam BigDecimal expense)
            throws SQLException {
        var user = authService.getCurrentUser();
        budgetService.addExpense(user.getEmail(), yearMonth, expense);
        return ResponseEntity.ok().build();
    }
}

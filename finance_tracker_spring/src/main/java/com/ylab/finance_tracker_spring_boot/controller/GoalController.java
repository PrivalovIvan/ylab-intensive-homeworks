package com.ylab.finance_tracker_spring_boot.controller;

import com.ylab.finance_tracker_spring_boot.domain.service.GoalService;
import com.ylab.finance_tracker_spring_boot.dto.GoalDTO;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import com.ylab.finance_tracker_spring_boot.annotation.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goals")
@Validated
@Loggable
@Tag(name = "Goal Controller", description = "API для работы с целями")
public class GoalController {
    private final GoalService goalService;
    private final AuthService authService;

    @Operation(
            summary = "Получение целей",
            description = "Получение всех целей пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список целей получен"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Цели не найдены"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<GoalDTO>> getAllGoals() throws SQLException {
        var goals = goalService.getUserGoals(authService.getCurrentUser().getEmail());
        return goals.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(goals);
    }


    @Operation(
            summary = "Создание цели",
            description = "Создание цели для пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Цель создана"
                    )
            }
    )
    @PostMapping(("/create"))
    public ResponseEntity<GoalDTO> createGoal(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания цели",
                    required = true
            )
            @Valid @RequestBody GoalDTO goalDTO)
            throws SQLException {
        goalService.createGoal(goalDTO);
        return ResponseEntity.ok(goalDTO);
    }

    @Operation(
            summary = "Обновление цели",
            description = "Пополнение суммы цели",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Цель успешно обновлена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Цели с указанным именем не существует"
                    )
            }
    )
    @PutMapping("/update")
    public ResponseEntity<GoalDTO> updateGoal(
            @Parameter(
                    description = "Название цели",
                    required = true
            )
            @RequestParam String name,
            @Parameter(
                    description = "Сумма пополнения цели",
                    required = true
            )
            @RequestParam BigDecimal amount)
            throws SQLException {
        goalService.updateGoal(name, amount);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удаление цели",
            description = "Удаление цели по ее названию",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Цель с таким названием удалена или ее не было"
                    )
            }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<GoalDTO> deleteGoal(
            @Parameter(
                    description = "Название цели",
                    required = true
            )
            @RequestParam String name)
            throws SQLException {
        goalService.deleteGoal(authService.getCurrentUser().getEmail(), name);
        return ResponseEntity.noContent().build();
    }
}

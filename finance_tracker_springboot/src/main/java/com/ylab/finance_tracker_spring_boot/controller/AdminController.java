package com.ylab.finance_tracker_spring_boot.controller;

import com.ylab.auditing.model.Action;
import com.ylab.auditing.repository.UserActionAuditRepository;
import com.ylab.finance_tracker_spring_boot.domain.service.AdministrationService;
import com.ylab.finance_tracker_spring_boot.dto.TransactionDTO;
import com.ylab.finance_tracker_spring_boot.dto.UserDTO;
import com.ylab.logging.annotation.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
@Loggable
@Tag(
        name = "Administration Controller",
        description = "API для управления пользователями и транзакций для администраторов")
public class AdminController {
    private final AdministrationService administrationService;
    private final UserActionAuditRepository userActionAuditRepository;

    @Operation(
            summary = "Получение пользователей",
            description = "Получение всех пользователей для администраторов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список пользователей получен"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Нет пользователей"
                    )
            }
    )
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() throws SQLException {
        List<UserDTO> users = administrationService.findAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Получение транзакций",
            description = "Получить все транзакции конкретного пользователя через его email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список транзакций получен"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "У пользователя нет транзакций"
                    )
            }
    )
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(
            @Parameter(
                    description = "Email пользователя, у которого будут получены транзакции",
                    required = true
            )
            @RequestParam String email)
            throws SQLException {
        List<TransactionDTO> transactions = administrationService.findAllTransactionsOfUsers(email);
        return transactions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Удаление пользователя по email",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Пользователь удален или не существовал"
                    )
            }
    )
    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String email) throws SQLException {
        administrationService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Получение списка действий пользователя по email",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Действия не найдены"
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список получен"
                    )
            }
    )

    @GetMapping("/audit_action_users")
    public ResponseEntity<List<Action>> getActionUsers(
            @RequestParam String email,
            @RequestParam(required = false) Optional<Integer> limit) {
        List<Action> actions = userActionAuditRepository.findAllActionUserEmail(email, limit.orElse(10));
        return actions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(actions);
    }
}

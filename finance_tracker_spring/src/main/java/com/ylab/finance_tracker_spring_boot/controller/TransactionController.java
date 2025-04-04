package com.ylab.finance_tracker_spring_boot.controller;

import com.ylab.finance_tracker_spring_boot.common.TransactionType;
import com.ylab.finance_tracker_spring_boot.domain.service.TransactionService;
import com.ylab.finance_tracker_spring_boot.dto.TransactionDTO;
import com.ylab.finance_tracker_spring_boot.dto.UserDTO;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import com.ylab.finance_tracker_spring_boot.annotation.Loggable;
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

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@Loggable
@Validated
@Tag(name = "Transaction Controller", description = "API для работы с транзакциями")
public class TransactionController {
    private final TransactionService transactionService;
    private final AuthService authService;


    @Operation(
            summary = "Получить транзакции",
            description = "Получение всех транзакций пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Транзакции получены",
                            content = @Content(
                                    schema = @Schema(implementation = TransactionDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Транзакции не найдены"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> showAllTransactions() throws SQLException {
        var transactions = transactionService.findAllTransactionUser(authService.getCurrentUser().getEmail());
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }


    @Operation(
            summary = "Создание транзакции",
            description = "Создание транзакции пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Транзакция создана"
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания транзакции",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TransactionDTO.class)
                    )
            )
            @Valid @RequestBody TransactionDTO transactionDTO) throws SQLException {
        transactionService.createTransaction(transactionDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получение транзакции по фильтру",
            description = "Фильтрация транзакций по дате",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Транзакции получены"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Транзакции не найдены"
                    )
            }
    )
    @GetMapping("/filter/date")
    public ResponseEntity<List<TransactionDTO>> filterTransactionDate(
            @Parameter(
                    description = "Дата для фильтрации транзакций",
                    required = true
            )
            @RequestParam String date) throws SQLException {
        var transactions = transactionService
                .findAllTransactionFilterByDate(authService.getCurrentUser().getEmail(), LocalDate.parse(date));
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Получение транзакции по фильтру",
            description = "Фильтрация транзакций по категории",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Транзакции получены"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Транзакции не найдены"
                    )
            }
    )
    @GetMapping("/filter/category")
    public ResponseEntity<List<TransactionDTO>> filterTransactionCategory(
            @Parameter(
                    description = "Категория для фильтрации транзакций",
                    required = true
            )
            @RequestParam String category) throws SQLException {
        var transactions = transactionService
                .findAllTransactionFilterByCategory(authService.getCurrentUser().getEmail(), category);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Получение транзакции по фильтру",
            description = "Фильтрация транзакций по типу транзакции (INCOME/EXPENSE)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Транзакции получены"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Транзакции не найдены"
                    )
            }
    )
    @GetMapping("/filter/type")
    public ResponseEntity<List<TransactionDTO>> filterTransactionType(
            @Parameter(
                    description = "Тип для фильтрации транзакций",
                    required = true
            )
            @RequestParam String type) throws SQLException {
        var transactions = transactionService
                .findAllTransactionFilterByType(authService.getCurrentUser().getEmail(), TransactionType.valueOf(type));
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }


    @Operation(
            summary = "Удаление транзакции",
            description = "удаление транзакции по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаление прошло успешно"
                    )
            }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTransaction(
            @Parameter(
                    description = "id транзакции, которая будет удалена",
                    required = true
            )
            @RequestParam String id) throws SQLException {

        UserDTO currentUser = authService.getCurrentUser();
        transactionService.deleteTransaction(currentUser.getEmail(), UUID.fromString(id));
        return ResponseEntity.ok().build();
    }
}

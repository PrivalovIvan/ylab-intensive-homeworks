package com.ylab.finance_tracker.usecase.dto;

import com.ylab.finance_tracker.common.TransactionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private UUID uuid;
    private String email;
    @NotNull(message = "Укажите тип транзакции (INCOME/EXPENSE)")
    private TransactionType type;
    @NotNull(message = "Сумма транзакции обязательна")
    @Positive(message = "Сумма должна быть положительной")
    private BigDecimal amount;
    @NotBlank(message = "Укажите категорию")
    private String category;
    private LocalDate date;
    @NotBlank(message = "Укажите описание")
    private String description;

    @Override
    public String toString() {
        return """
                Transaction:
                id: %s
                date: %s
                email: %s
                type: %s
                amount: %s
                category: %s
                description: %s""".formatted(uuid, date, email, type, amount, category, description);
    }
}

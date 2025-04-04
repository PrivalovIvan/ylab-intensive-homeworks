package com.ylab.finance_tracker_spring_boot.dto;

import com.ylab.finance_tracker_spring_boot.common.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private final UUID uuid;
    private final String email;
    @NotNull(message = "Specify the type of transaction (INCOME/EXPENSE)")
    private final TransactionType type;
    @NotNull(message = "The transaction amount is required")
    @Positive(message = "The amount must be positive")
    private final BigDecimal amount;
    @NotBlank(message = "Specify the category")
    private final String category;
    private final LocalDate date;
    @NotBlank(message = "Provide a description")
    private final String description;

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

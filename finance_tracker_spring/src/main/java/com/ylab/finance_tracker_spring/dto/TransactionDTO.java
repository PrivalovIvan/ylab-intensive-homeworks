package com.ylab.finance_tracker_spring.dto;

import com.ylab.finance_tracker_spring.common.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull(message = "Specify the type of transaction (INCOME/EXPENSE)")
    private TransactionType type;
    @NotNull(message = "The transaction amount is required")
    @Positive(message = "The amount must be positive")
    private BigDecimal amount;
    @NotBlank(message = "Specify the category")
    private String category;
    private LocalDate date;
    @NotBlank(message = "Provide a description")
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

package com.ylab.finance_tracker_spring_boot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class BudgetDTO {
    private final UUID uuid;
    private final String email;
    @NotNull(message = "Enter the year and month in the YYYY-MM format.")
    private final YearMonth yearMonth;
    @NotNull(message = "The monthly budget amount is required")
    @Positive(message = "The amount must be positive")
    private final BigDecimal budget;
    private final BigDecimal spent;

    @Override
    public String toString() {
        return """
                Budget:
                    uuid: %s
                    email: %s
                    yearMonth: %s
                    budget: %s
                    spent: %s
                """.formatted(uuid, email, yearMonth, budget, spent);
    }
}

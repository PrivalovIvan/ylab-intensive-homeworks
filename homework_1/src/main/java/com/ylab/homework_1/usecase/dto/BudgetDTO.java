package com.ylab.homework_1.usecase.dto;

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
    private final YearMonth yearMonth;
    private final BigDecimal budget;
    private BigDecimal spent;

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

package com.ylab.homework_1.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@AllArgsConstructor
public class BudgetDTO {
    private final String email;
    private final YearMonth yearMonth;
    private final BigDecimal budget;
    private BigDecimal spent;

    @Override
    public String toString() {
        return """
                Budget:
                    email: %s
                    yearMonth: %s
                    budget: %s
                    spent: %s
                """.formatted(email, yearMonth, budget, spent);
    }
}

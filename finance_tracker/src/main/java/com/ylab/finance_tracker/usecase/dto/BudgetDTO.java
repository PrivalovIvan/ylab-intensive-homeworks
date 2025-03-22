package com.ylab.finance_tracker.usecase.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetDTO {
    private UUID uuid;
    private String email;
    @NotNull(message = "Введите год и месяц в формате YYYY-MM")
    private YearMonth yearMonth;
    @NotNull(message = "Сумма бюджета на месяц обязательна")
    @Positive(message = "Сумма должна быть положительной")
    private BigDecimal budget;
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

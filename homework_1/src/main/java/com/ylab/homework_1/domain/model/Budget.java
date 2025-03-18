package com.ylab.homework_1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Budget {
    private final UUID uuid;
    private final String email;
    private final YearMonth yearMonth;
    private final BigDecimal budget;
    private BigDecimal spent;

    public boolean isExceeded() {
        return spent.compareTo(budget) > 0;
    }

    @Override
    public String toString() {
        return """
                Budget:
                    id: %s
                    email: %s
                    yearMonth: %s
                    budget: %s
                    spent: %s
                """.formatted(uuid, email, yearMonth, budget, spent);
    }
}

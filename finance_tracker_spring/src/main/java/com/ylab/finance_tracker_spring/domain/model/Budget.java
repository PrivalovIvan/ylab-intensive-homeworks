package com.ylab.finance_tracker_spring.domain.model;

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
public class Budget {
    private UUID uuid;
    private String email;
    private YearMonth yearMonth;
    private BigDecimal budget;
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

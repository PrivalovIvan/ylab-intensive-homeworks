package com.ylab.homework_1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Budget {
    private final String email;
    private final YearMonth yearMonth;
    private final BigDecimal budget;
    private BigDecimal spent;

    public void addExpense(BigDecimal amount) {
        this.spent = this.spent.add(amount);
    }

    public boolean isExceeded() {
        return spent.compareTo(budget) > 0;
    }

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

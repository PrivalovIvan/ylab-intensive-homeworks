package com.ylab.homework_1.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GoalDTO {
    private String email;
    private String title;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;

    @Override
    public String toString() {
        return """
                Goal:
                    email: %s
                    title: %s
                    targetAmount: %s
                    savedAmount: %s
                """.formatted(email, title, targetAmount, savedAmount);
    }
}

package com.ylab.homework_1.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class GoalDTO {
    private UUID uuid;
    private String email;
    private String title;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;

    @Override
    public String toString() {
        return """
                Goal:
                    id: %s
                    email: %s
                    title: %s
                    targetAmount: %s
                    savedAmount: %s
                """.formatted(uuid, email, title, targetAmount, savedAmount);
    }
}

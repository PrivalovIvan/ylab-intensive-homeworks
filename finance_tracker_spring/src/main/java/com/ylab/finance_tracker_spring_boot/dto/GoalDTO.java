package com.ylab.finance_tracker_spring_boot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class GoalDTO {
    private final UUID uuid;
    private final String email;
    @NotBlank(message = "Specify the name of the goal")
    private final String title;
    @NotNull(message = "Be sure to specify the amount")
    private final BigDecimal targetAmount;
    private final BigDecimal savedAmount;

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

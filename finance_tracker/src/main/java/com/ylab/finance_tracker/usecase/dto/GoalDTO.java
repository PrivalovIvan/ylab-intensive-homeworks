package com.ylab.finance_tracker.usecase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalDTO {
    private UUID uuid;
    private String email;
    @NotBlank(message = "Укажите название цели")
    private String title;
    @NotNull(message = "Обязательно укажите сумму")
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

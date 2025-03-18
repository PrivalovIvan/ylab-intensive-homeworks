package com.ylab.homework_1.usecase.dto;

import com.ylab.homework_1.common.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private UUID uuid;
    private String email;
    private TransactionType type;
    private BigDecimal amount;
    private String category;
    private String nameGoal;
    private LocalDate date;
    private String description;

    @Override
    public String toString() {
        return """
                Transaction:
                id: %s
                date: %s
                email: %s
                type: %s
                amount: %s
                category: %s
                description: %s""".formatted(uuid, date, email, type, amount, category, description);
    }
}

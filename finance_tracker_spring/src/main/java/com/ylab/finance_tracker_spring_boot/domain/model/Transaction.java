package com.ylab.finance_tracker_spring_boot.domain.model;

import com.ylab.finance_tracker_spring_boot.common.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private UUID uuid;
    private String email;
    private TransactionType type;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private String description;

    @Override
    public String toString() {
        return """
                Transaction:
                 uuid: %s
                 date: %s
                 email: %s
                 type: %s
                 amount: %s
                 category: %s
                 description: %s
                """.formatted(uuid, date, email, type, amount, category, description);
    }
}

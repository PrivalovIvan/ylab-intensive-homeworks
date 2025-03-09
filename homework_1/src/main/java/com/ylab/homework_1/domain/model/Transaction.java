package com.ylab.homework_1.domain.model;

import com.ylab.homework_1.common.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Transaction {
    private final UUID uuid = UUID.randomUUID();
    private final String email;
    private final TransactionType type;
    private BigDecimal amount;
    private String category;
    private final LocalDate date = LocalDate.now();
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

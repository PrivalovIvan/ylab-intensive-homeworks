package com.ylab.homework_1.datasource.model;

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
public class TransactionDB {
    private final UUID uuid;
    private final String email;
    private TransactionType type;
    private BigDecimal amount;
    private String category;
    private final LocalDate date;
    private String description;
}

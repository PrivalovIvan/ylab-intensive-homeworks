package com.ylab.homework_1.datasource.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GoalDB {
    private final String email;
    private final String title;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
}

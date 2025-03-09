package com.ylab.homework_1.datasource.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BudgetDB {
    private final String email;
    private final YearMonth yearMonth;
    private BigDecimal budget;
    private BigDecimal spent;

}

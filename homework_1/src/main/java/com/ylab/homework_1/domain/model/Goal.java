package com.ylab.homework_1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Goal {
    private final String email;
    private final String title;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;

    public void addAmount(BigDecimal amount) {
        this.savedAmount = this.savedAmount.add(amount);
    }

    public boolean isAchieved() {
        return savedAmount.compareTo(targetAmount) >= 0;
    }

}

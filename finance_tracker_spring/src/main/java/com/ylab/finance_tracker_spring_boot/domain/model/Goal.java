package com.ylab.finance_tracker_spring_boot.domain.model;

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
public class Goal {
    private UUID uuid;
    private String email;
    private String title;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
}

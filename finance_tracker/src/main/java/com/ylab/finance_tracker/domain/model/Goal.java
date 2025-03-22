package com.ylab.finance_tracker.domain.model;

import lombok.*;

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

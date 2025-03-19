package com.ylab.homework_1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {
    private final UUID uuid;
    private final String email;
    private final String title;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;

}

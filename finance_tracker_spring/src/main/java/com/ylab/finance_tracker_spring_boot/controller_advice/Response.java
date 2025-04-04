package com.ylab.finance_tracker_spring_boot.controller_advice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Response {
    private LocalDateTime timestamp;
    private String message;
}

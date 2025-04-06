package com.ylab.auditing.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Action(UUID uuid, String userEmail, String action, Instant actionDate) {
}
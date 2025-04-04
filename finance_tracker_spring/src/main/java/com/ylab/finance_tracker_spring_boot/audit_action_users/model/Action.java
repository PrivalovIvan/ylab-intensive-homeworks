package com.ylab.finance_tracker_spring_boot.audit_action_users.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Action(UUID uuid, String userEmail, String action, Instant actionDate) {
}

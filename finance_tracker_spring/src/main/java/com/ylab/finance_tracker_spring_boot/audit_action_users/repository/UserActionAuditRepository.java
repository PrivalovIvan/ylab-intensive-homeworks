package com.ylab.finance_tracker_spring_boot.audit_action_users.repository;

import com.ylab.finance_tracker_spring_boot.audit_action_users.model.Action;

import java.util.List;

public interface UserActionAuditRepository {
    void save(Action action);

    List<Action> findAllActionUserEmail(String email, int limit);
}

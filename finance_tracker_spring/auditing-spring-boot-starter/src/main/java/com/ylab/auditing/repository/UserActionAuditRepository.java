package com.ylab.auditing.repository;

import com.ylab.auditing.model.Action;

import java.util.List;

public interface UserActionAuditRepository {
    void save(Action action);

    List<Action> findAllActionUserEmail(String email, int limit);
}
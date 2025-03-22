package com.ylab.finance_tracker.usecase.service;

public interface NotificationService {
    void send(String email, String msg);
}

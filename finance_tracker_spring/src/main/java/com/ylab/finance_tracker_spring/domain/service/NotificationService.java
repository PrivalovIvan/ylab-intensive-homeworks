package com.ylab.finance_tracker_spring.domain.service;

public interface NotificationService {
    void send(String email, String msg);
}

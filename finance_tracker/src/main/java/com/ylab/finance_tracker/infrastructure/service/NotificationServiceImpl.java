package com.ylab.finance_tracker.infrastructure.service;

import com.ylab.finance_tracker.usecase.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {
    @Override
    public void send(String email, String msg) {
        System.out.println(email + ": " + msg);
    }
}

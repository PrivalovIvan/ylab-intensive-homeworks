package com.ylab.homework_1.application.service;

import com.ylab.homework_1.domain.service.NotificationService;

public class EmailNotificationService implements NotificationService {
    @Override
    public void send(String email, String msg) {
        System.out.println(email + ": " + msg);
    }
}

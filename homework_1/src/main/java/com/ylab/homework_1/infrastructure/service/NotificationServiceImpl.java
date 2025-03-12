package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.usecase.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {
    @Override
    public void send(String email, String msg) {
        System.out.println(email + ": " + msg);
    }
}

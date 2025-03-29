package com.ylab.finance_tracker_spring.service;

import com.ylab.finance_tracker_spring.domain.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void send(String email, String msg) {
        System.out.println(email + ": " + msg);
    }
}

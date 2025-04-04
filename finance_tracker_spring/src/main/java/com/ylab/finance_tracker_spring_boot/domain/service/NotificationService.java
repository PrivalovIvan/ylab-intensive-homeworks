package com.ylab.finance_tracker_spring_boot.domain.service;

public interface NotificationService {
    //    void send(String email, String msg);
    void sendEmail(String to, String subject, String body);
}

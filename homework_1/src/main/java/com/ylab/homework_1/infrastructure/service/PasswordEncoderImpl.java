package com.ylab.homework_1.infrastructure.service;

import com.ylab.homework_1.usecase.service.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("rawPassword cannot be null or empty");
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            throw new IllegalArgumentException("rawPassword and encodedPassword cannot be null");
        }
        try {
            return BCrypt.checkpw(rawPassword, encodedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}

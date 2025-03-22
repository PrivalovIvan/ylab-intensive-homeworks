package com.ylab.finance_tracker.ui.validator;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@FunctionalInterface
public interface ValidatorDTO {
    <T> boolean isValid(T t, HttpServletResponse response) throws IOException;
}

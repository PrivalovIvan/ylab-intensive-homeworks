package com.ylab.finance_tracker.ui.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class ValidInputData implements ValidatorDTO {
    private final Validator validator;
    private final ObjectMapper objectMapper;

    @Override
    public <T> boolean isValid(T t, HttpServletResponse response) throws IOException {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter writer = response.getWriter()) {
                List<String> errorMessages = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .toList();
                writer.write(objectMapper.writeValueAsString(Map.of("errors", errorMessages)));
                return false;
            }
        }
        return true;
    }
}

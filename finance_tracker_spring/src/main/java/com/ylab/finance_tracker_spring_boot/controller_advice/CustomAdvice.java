package com.ylab.finance_tracker_spring_boot.controller_advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;

@ControllerAdvice
public class CustomAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        Response response = new Response(LocalDateTime.now(), e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Response> sqlExceptionHandler(SQLException e) {
        Response response = new Response(LocalDateTime.now(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

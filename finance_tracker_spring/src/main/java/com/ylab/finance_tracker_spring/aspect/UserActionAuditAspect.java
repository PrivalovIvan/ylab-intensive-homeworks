package com.ylab.finance_tracker_spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class UserActionAuditAspect {

    @AfterReturning("execution(* com.ylab.finance_tracker_spring.service.*.*(..))")
    public void logAfterReturning(JoinPoint joinPoint) {
        log.info("User action executed: {}", joinPoint.getSignature().getName());
    }
}

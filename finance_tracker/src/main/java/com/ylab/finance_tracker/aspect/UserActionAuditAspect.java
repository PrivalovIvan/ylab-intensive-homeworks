package com.ylab.finance_tracker.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class UserActionAuditAspect {

    @AfterReturning("execution(* com.ylab.finance_tracker.usecase.service.*.*(..))")
    public void logAfterReturning(JoinPoint joinPoint) {
        log.info("User action executed: {}", joinPoint.getSignature().getName());
    }
}

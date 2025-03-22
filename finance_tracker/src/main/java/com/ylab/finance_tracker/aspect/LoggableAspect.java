package com.ylab.finance_tracker.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class LoggableAspect {
    @Pointcut("within(@com.ylab.finance_tracker.annotation.Loggable *) && execution(* * (..))")
    public void loggablePointcut() {
    }

    @Around("loggablePointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("Execution time of {}: {}", joinPoint.getSignature().getName(), executionTime);
        return result;
    }
}

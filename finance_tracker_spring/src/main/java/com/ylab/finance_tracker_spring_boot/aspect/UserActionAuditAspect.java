package com.ylab.finance_tracker_spring_boot.aspect;

import com.ylab.finance_tracker_spring_boot.audit_action_users.model.Action;
import com.ylab.finance_tracker_spring_boot.audit_action_users.repository.UserActionAuditRepository;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionAuditAspect {
    private final UserActionAuditRepository userActionAuditRepository;
    private final AuthService authService;

    @AfterReturning("execution(* com.ylab.finance_tracker_spring_boot.service.*.*(..)) " +
            "&& !execution(* com.ylab.finance_tracker_spring_boot.service.UserServiceImpl.findByEmail(..))")
    public void logAfterReturning(JoinPoint joinPoint) {
        userActionAuditRepository.save(
                Action.builder()
                        .userEmail(authService.getCurrentUser().getEmail())
                        .action(joinPoint.getSignature().getName())
                        .actionDate(Instant.now())
                        .build());
    }
}

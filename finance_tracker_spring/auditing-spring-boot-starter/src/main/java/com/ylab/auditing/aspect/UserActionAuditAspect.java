package com.ylab.auditing.aspect;

import com.ylab.auditing.model.Action;
import com.ylab.auditing.provider.CurrentUserProvider;
import com.ylab.auditing.repository.UserActionAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.time.Instant;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class UserActionAuditAspect {
    private final UserActionAuditRepository userActionAuditRepository;
    private final CurrentUserProvider currentUserProvider;

    @AfterReturning("execution(* com.ylab.finance_tracker_spring_boot.service.*.*(..)) " +
            "&& !execution(* com.ylab.finance_tracker_spring_boot.service.UserServiceImpl.findByEmail(..))")
    public void logAfterReturning(JoinPoint joinPoint) {
        String userEmail = currentUserProvider.getCurrentUserIdentifier().orElse("anonymousUser");
        userActionAuditRepository.save(
                Action.builder()
                        .userEmail(userEmail)
                        .action(joinPoint.getSignature().getName())
                        .actionDate(Instant.now())
                        .build());
    }
}

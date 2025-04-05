package com.ylab.auditing.config;

import com.ylab.auditing.aspect.UserActionAuditAspect;
import com.ylab.auditing.provider.CurrentUserProvider;
import com.ylab.auditing.repository.UserActionAuditRepository;
import com.ylab.auditing.repository.UserActionAuditRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AuditingConfig {
    @Bean
    public UserActionAuditAspect userActionAuditAspect(UserActionAuditRepository userActionAuditRepository, CurrentUserProvider currentUserProvider) {
        return new UserActionAuditAspect(userActionAuditRepository, currentUserProvider);
    }

    @Bean
    public UserActionAuditRepository userActionAuditRepository(JdbcTemplate jdbcTemplate) {
        return new UserActionAuditRepositoryImpl(jdbcTemplate);
    }
}

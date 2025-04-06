package com.ylab.finance_tracker_spring_boot.provider;

import com.ylab.auditing.provider.CurrentUserProvider;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthCurrentUserProvider implements CurrentUserProvider {
    private final AuthService authService;

    @Override
    public Optional<String> getCurrentUserIdentifier() throws SQLException {
        return Optional.ofNullable(authService.getCurrentUser().getEmail());
    }
}

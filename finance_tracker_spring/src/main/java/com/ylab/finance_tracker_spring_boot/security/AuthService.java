package com.ylab.finance_tracker_spring_boot.security;

import com.ylab.finance_tracker_spring_boot.domain.service.UserService;
import com.ylab.finance_tracker_spring_boot.dto.UserDTO;
import com.ylab.finance_tracker_spring_boot.security_canon.JwtService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final HttpSession session;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public boolean authenticate(String email, String password) throws SQLException {
        return userService.findByEmail(email)
                .map(user -> {
                    if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                        var auth = new CustomAuthentication(user);
                        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                        SecurityContextHolder.getContext().setAuthentication(auth);
//                        session.setAttribute("user", user);
                        return true;
                    }
                    return false;
                }).orElse(false);
    }

    public UserDTO getCurrentUser() throws SQLException {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserDTO user = userService.findByEmail(email).orElse(null);
        return user;
    }
}

record CustomAuthentication(UserDTO user) implements Authentication {
    @Override
    public Object getPrincipal() {
        return user.getEmail();
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}

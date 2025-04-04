package com.ylab.finance_tracker_spring_boot.security;

import com.ylab.finance_tracker_spring_boot.domain.service.UserService;
import com.ylab.finance_tracker_spring_boot.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserService userService;
    private final HttpSession session;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticate(String email, String password) throws SQLException {
        return userService.findByEmail(email)
                .map(user -> {
                    if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                        session.setAttribute("user", user);
                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, password));
                        return true;
                    }
                    return false;
                }).orElse(false);
    }

    public UserDTO getCurrentUser() {
        return (UserDTO) session.getAttribute("user");
    }
}

record CustomAuthentication(UserDTO user) implements Authentication {
    @Override
    public Object getPrincipal() {
        return user;
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
        return List.of();
    }
}

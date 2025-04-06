package com.ylab.finance_tracker_spring.dto;

import com.ylab.finance_tracker_spring.common.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO implements UserDetails {
    private UUID uuid;
    @NotBlank(message = "Enter your name")
    private String name;
    @NotBlank(message = "Enter your email address")
    @Email(message = "Enter the correct email address")
    private String email;
    @NotBlank(message = "Enter the password")
    @Size(min = 4, message = "The password must be longer than 4 characters.")
    private String password;
    private Role role;

    @Override
    public String toString() {
        return """
                User:
                    id: %s
                    name: %s,
                    email: %s,
                    password: %s,
                    role: %s
                """.formatted(uuid, name, email, password, role);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
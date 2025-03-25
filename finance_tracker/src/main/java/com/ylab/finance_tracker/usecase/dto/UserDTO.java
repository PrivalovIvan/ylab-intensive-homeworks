package com.ylab.finance_tracker.usecase.dto;

import com.ylab.finance_tracker.common.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
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
}
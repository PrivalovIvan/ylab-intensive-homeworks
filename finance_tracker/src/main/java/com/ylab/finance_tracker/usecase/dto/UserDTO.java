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
    @NotBlank(message = "Введите ваше имя")
    private String name;
    @NotBlank(message = "Введите ваш email")
    @Email(message = "Введите корректный email")
    private String email;
    @NotBlank(message = "Введите пароль")
    @Size(min = 4, message = "Пароль должен быть длиннее 4х символов")
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
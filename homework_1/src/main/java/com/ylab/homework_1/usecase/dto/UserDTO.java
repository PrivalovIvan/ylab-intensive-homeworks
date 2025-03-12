package com.ylab.homework_1.usecase.dto;

import com.ylab.homework_1.common.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private Role role;

    @Override
    public String toString() {
        return """
                User:
                    id: %s,
                    name: %s,
                    email: %s,
                    password: %s,
                    role: %s
                """.formatted(id, name, email, password, role);
    }
}
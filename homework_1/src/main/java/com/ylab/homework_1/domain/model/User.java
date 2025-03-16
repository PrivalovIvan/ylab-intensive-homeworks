package com.ylab.homework_1.domain.model;

import com.ylab.homework_1.common.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class User {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    @Override
    public String toString() {
        return """
                Profile :
                    id: %s
                    Name: %s,
                    Email: %s,
                    Password: %s,
                    Role: %s
                """.formatted(id, name, email, password, role.name());
    }
}

package com.ylab.homework_1.domain.model;

import com.ylab.homework_1.common.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    @Override
    public String toString() {
        return """
                Profile :
                    Name: %s,
                    Email: %s,
                    Password: %s,
                    Role: %s
                """.formatted(name, email, password, role.name());
    }
}

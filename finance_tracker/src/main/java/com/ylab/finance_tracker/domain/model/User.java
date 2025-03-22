package com.ylab.finance_tracker.domain.model;

import com.ylab.finance_tracker.common.Role;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private UUID uuid;
    private String name;
    private String email;
    private String password;
    private Role role;

    @Override
    public String toString() {
        return """
                Profile :
                    id: %s
                    Name: %s,
                    Email: %s,
                    Password: %s,
                    Role: %s
                """.formatted(uuid, name, email, password, role.name());
    }
}

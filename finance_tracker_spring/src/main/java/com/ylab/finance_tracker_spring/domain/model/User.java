package com.ylab.finance_tracker_spring.domain.model;

import com.ylab.finance_tracker_spring.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

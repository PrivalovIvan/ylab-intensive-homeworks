package com.ylab.homework_1.datasource.model;

import com.ylab.homework_1.common.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDB {
    private final String name;
    private final String email;
    private final String password;
    private final Role role;
}

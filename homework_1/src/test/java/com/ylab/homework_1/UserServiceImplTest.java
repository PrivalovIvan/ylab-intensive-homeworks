package com.ylab.homework_1;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.infrastructure.repository.UserRepositoryImpl;
import com.ylab.homework_1.infrastructure.service.UserServiceImpl;
import com.ylab.homework_1.usecase.dto.UserDTO;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserServiceImplTest {
    private static UserServiceImpl userService;

    @BeforeAll
    static void setUp() throws Exception {
        PostgresDataSource.initDB(TestContainerConfig.getProperties());

        try (var connection = PostgresDataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/migration/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        }

        userService = new UserServiceImpl(new UserRepositoryImpl());
    }

    @Test
    void testRegisterAndFindAll() throws SQLException {
        UserDTO user = new UserDTO(null, "testUser", "test@example.com", "password", Role.USER);
        userService.register(user);

        List<UserDTO> users = userService.findAll();
        assertFalse(users.isEmpty(), "Users list should not be empty");
        assertTrue(users.stream().anyMatch(u -> "test@example.com".equals(u.getEmail())), "Registered user should be in the list");
    }

    @Test
    void testFindByEmail() throws SQLException {
        UserDTO user = new UserDTO(null, "findUser", "find@example.com", "password", Role.USER);
        userService.register(user);

        Optional<UserDTO> foundUser = Optional.of(userService.findByEmail("find@example.com"));
        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals("find@example.com", foundUser.get().getEmail(), "Email should match");
    }

    @Test
    void testDelete() throws SQLException {
        UserDTO user = new UserDTO(null, "deleteUser", "delete@example.com", "password", Role.USER);
        userService.register(user);

        UserDTO foundUser = userService.findByEmail("delete@example.com");
        assertNotNull(foundUser, "User should exist before deletion");
        assertEquals("delete@example.com", foundUser.getEmail(), "Email should match");

        userService.delete("delete@example.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.findByEmail("delete@example.com"));
        assertEquals("Email: delete@example.com not found", exception.getMessage(), "Exception message should match");
    }
}
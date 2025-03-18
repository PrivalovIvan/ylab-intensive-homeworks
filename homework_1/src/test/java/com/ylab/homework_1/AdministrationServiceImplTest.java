package com.ylab.homework_1;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.infrastructure.repository.TransactionRepositoryImpl;
import com.ylab.homework_1.infrastructure.repository.UserRepositoryImpl;
import com.ylab.homework_1.infrastructure.service.AdministrationServiceImpl;
import com.ylab.homework_1.infrastructure.service.TransactionServiceImpl;
import com.ylab.homework_1.infrastructure.service.UserServiceImpl;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.dto.UserDTO;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class AdministrationServiceImplTest {
    private static AdministrationServiceImpl adminService;
    private static UserServiceImpl userService;
    private static TransactionServiceImpl transactionService;

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
        transactionService = new TransactionServiceImpl(new TransactionRepositoryImpl());
        adminService = new AdministrationServiceImpl(userService, transactionService);

        userService.register(new UserDTO(null, "testUser", "testt@example.com", "password", Role.USER));

    }

    @Test
    void testFindAllUsers() throws SQLException {
        List<UserDTO> users = adminService.findAllUsers();
        System.out.println(userService.findAll());

        assertFalse(users.isEmpty(), "List of users should not be empty");
        assertEquals(5, users.size(), "Should return one user");
        assertEquals("bob@example.com", users.get(0).getEmail(), "Email should match");
        assertEquals(Role.USER, users.get(0).getRole(), "Role should be USER");
    }

    @Test
    void testFindAllTransactionsOfUsers() throws SQLException {
        TransactionDTO transaction = TransactionDTO.builder()
                .email("testt@example.com")
                .type(TransactionType.INCOME)
                .amount(BigDecimal.valueOf(100))
                .category("Test")
                .date(LocalDate.now())
                .description("Test transaction").build();
        transactionService.createTransaction(transaction);

        List<TransactionDTO> transactions = adminService.findAllTransactionsOfUsers("testt@example.com");
        assertFalse(transactions.isEmpty(), "List of transactions should not be empty");
        assertEquals(1, transactions.size(), "Should return one transaction");
        assertEquals(new BigDecimal("100.00"), transactions.get(0).getAmount(), "Amount should match");
        transactionService.deleteTransaction("test@example.com", transactions.get(0).getUuid());
    }

    @Test
    void testDeleteUser() throws SQLException {
        UserDTO user = new UserDTO(null, "deleteUser", "delete@example.com", "password", Role.USER);
        userService.register(user);

        UserDTO foundUser = userService.findByEmail("delete@example.com");
        assertNotNull(foundUser, "User should exist before deletion");

        adminService.deleteUser("delete@example.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.findByEmail("delete@example.com"));
        assertEquals("Email: delete@example.com not found", exception.getMessage());
    }

    @Test
    void testDeleteUserNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> adminService.deleteUser("nonexistent@example.com"));
        assertEquals("Email: nonexistent@example.com not found", exception.getMessage());
    }

    @Test
    void testDeleteAdminUser() throws SQLException {
        userService.register(new UserDTO(null, "admin2", "admin2@example.com", "adminpass", Role.ADMIN));

        Exception exception = assertThrows(IllegalStateException.class, () -> adminService.deleteUser("admin2@example.com"));
        assertEquals("Cannot delete an admin user: admin2@example.com", exception.getMessage());
    }
}
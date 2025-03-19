package com.ylab.homework_1;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.infrastructure.repository.TransactionRepositoryImpl;
import com.ylab.homework_1.infrastructure.service.TransactionServiceImpl;
import com.ylab.homework_1.usecase.dto.TransactionDTO;
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
class TransactionServiceImplTest {
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

        transactionService = new TransactionServiceImpl(new TransactionRepositoryImpl());
    }

    @Test
    void testCreateAndFindTransactions() throws SQLException {
        TransactionDTO transaction = new TransactionDTO(null, "test@example.com", TransactionType.INCOME,
                BigDecimal.valueOf(100), "Test", null, LocalDate.now(), "Test transaction");

        transactionService.createTransaction(transaction);

        List<TransactionDTO> transactions = transactionService.findAllTransactionUser("test@example.com");
        assertFalse(transactions.isEmpty(), "Transactions list should not be empty");
        assertEquals(1, transactions.size(), "Should return one transaction");
        assertEquals(new BigDecimal("100.00"), transactions.get(0).getAmount(), "Amount should match");
    }
}
package com.ylab.homework_1;

import com.ylab.homework_1.common.TransactionType;
import com.ylab.homework_1.datasource.mapper.TransactionDBMapperImpl;
import com.ylab.homework_1.datasource.model.TransactionDB;
import com.ylab.homework_1.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class TransactionDBMapperImplTest {
    private TransactionDBMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new TransactionDBMapperImpl();
    }

    @Test
    void toTransaction_convertsTransactionDB() {
        UUID uuid = UUID.randomUUID();
        TransactionDB transactionDB = new TransactionDB(uuid, "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", LocalDate.of(2025, 3, 10), "Groceries");

        Optional<Transaction> result = mapper.toTransaction(transactionDB);

        assertThat(result).isPresent();
        Transaction transaction = result.get();
        assertThat(transaction.getEmail()).isEqualTo("user@example.com");
        assertThat(transaction.getType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(transaction.getAmount()).isEqualTo(BigDecimal.valueOf(300));
        assertThat(transaction.getCategory()).isEqualTo("Food");
        assertThat(transaction.getDescription()).isEqualTo("Groceries");
    }

    @Test
    void toTransaction_returnsEmptyForNull() {
        Optional<Transaction> result = mapper.toTransaction(null);

        assertThat(result).isEmpty();
    }

    @Test
    void toTransactionDB_convertsTransaction() {
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly") {
            @Override
            public UUID getUuid() {
                return uuid;
            }

            @Override
            public LocalDate getDate() {
                return LocalDate.of(2025, 3, 5);
            }
        };

        Optional<TransactionDB> result = mapper.toTransactionDB(transaction);

        assertThat(result).isPresent();
        TransactionDB transactionDB = result.get();
        assertThat(transactionDB.getUuid()).isEqualTo(uuid);
        assertThat(transactionDB.getEmail()).isEqualTo("user@example.com");
        assertThat(transactionDB.getType()).isEqualTo(TransactionType.INCOME);
        assertThat(transactionDB.getAmount()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(transactionDB.getCategory()).isEqualTo("Salary");
        assertThat(transactionDB.getDate()).isEqualTo(LocalDate.of(2025, 3, 5));
        assertThat(transactionDB.getDescription()).isEqualTo("Monthly");
    }

    @Test
    void toTransactionDB_returnsEmptyForNull() {
        Optional<TransactionDB> result = mapper.toTransactionDB(null);

        assertThat(result).isEmpty();
    }
}
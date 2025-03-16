//package com.ylab.homework_1;
//
//import com.ylab.homework_1.common.TransactionType;
//import com.ylab.homework_1.datasource.mapper.TransactionDBMapper;
//import com.ylab.homework_1.datasource.model.TransactionDB;
//import com.ylab.homework_1.datasource.repository.TransactionRepositoryImpl;
//import com.ylab.homework_1.domain.model.Transaction;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//class TransactionRepositoryImplTest {
//    private TransactionDBMapper transactionDBMapper;
//    private TransactionRepositoryImpl repository;
//
//    @BeforeEach
//    void setUp() {
//        transactionDBMapper = Mockito.mock(TransactionDBMapper.class);
//        repository = new TransactionRepositoryImpl(transactionDBMapper);
//    }
//
//    @Test
//    void getTransactions_returnsAllTransactions() {
//        TransactionDB transactionDB = new TransactionDB(UUID.randomUUID(), "user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", LocalDate.now(), "Monthly");
//        Transaction transaction = new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly");
//        repository.getTransactions().put(transactionDB.getUuid(), transactionDB);
//        Mockito.when(transactionDBMapper.toTransaction(transactionDB)).thenReturn(Optional.of(transaction));
//
//        List<Transaction> result = repository.findAll(transaction);
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0)).isEqualTo(transaction);
//    }
//
//    @Test
//    void create_addsTransaction() {
//        Transaction transaction = new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries");
//        TransactionDB transactionDB = new TransactionDB(transaction.getUuid(), "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", transaction.getDate(), "Groceries");
//        Mockito.when(transactionDBMapper.toTransactionDB(transaction)).thenReturn(Optional.of(transactionDB));
//
//        repository.create(transaction);
//
//        Assertions.assertThat(repository.getTransactions()).containsKey(transaction.getUuid());
//    }
//
//    @Test
//    void findAll_throwsExceptionForNull() {
//        Assertions.assertThatThrownBy(() -> repository.findAll(null))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("transaction cannot be null");
//    }
//
//    @Test
//    void findAll_returnsAllTransactions() {
//        Transaction transaction = new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly");
//        TransactionDB transactionDB = new TransactionDB(transaction.getUuid(), "user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", transaction.getDate(), "Monthly");
//        repository.getTransactions().put(transactionDB.getUuid(), transactionDB);
//        Mockito.when(transactionDBMapper.toTransaction(transactionDB)).thenReturn(Optional.of(transaction));
//
//        List<Transaction> result = repository.findAll(transaction);
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0)).isEqualTo(transaction);
//    }
//
//    @Test
//    void findByUserEmail_filtersByEmail() {
//        TransactionDB transactionDB1 = new TransactionDB(UUID.randomUUID(), "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", LocalDate.now(), "Groceries");
//        TransactionDB transactionDB2 = new TransactionDB(UUID.randomUUID(), "other@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", LocalDate.now(), "Monthly");
//        Transaction transaction1 = new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries");
//        repository.getTransactions().put(transactionDB1.getUuid(), transactionDB1);
//        repository.getTransactions().put(transactionDB2.getUuid(), transactionDB2);
//        Mockito.when(transactionDBMapper.toTransaction(transactionDB1)).thenReturn(Optional.of(transaction1));
//        Mockito.when(transactionDBMapper.toTransaction(transactionDB2)).thenReturn(Optional.empty());
//
//        List<Transaction> result = repository.findByUserEmail("user@example.com");
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getEmail()).isEqualTo("user@example.com");
//    }
//
//    @Test
//    void findByUUID_filtersByUuid() {
//        UUID uuid = UUID.randomUUID();
//        TransactionDB transactionDB = new TransactionDB(uuid, "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", LocalDate.now(), "Groceries");
//        Transaction transaction = new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries") {
//            @Override
//            public UUID getUuid() {
//                return uuid; // Переопределяем UUID
//            }
//        };
//        repository.getTransactions().put(uuid, transactionDB);
//        Mockito.when(transactionDBMapper.toTransaction(transactionDB)).thenReturn(Optional.of(transaction));
//
//        List<Transaction> result = repository.findByUUID(uuid);
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getUuid()).isEqualTo(uuid);
//    }
//
//    @Test
//    void update_modifiesExistingTransaction() {
//        UUID uuid = UUID.randomUUID();
//        TransactionDB transactionDB = new TransactionDB(uuid, "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", LocalDate.now(), "Groceries");
//        repository.getTransactions().put(uuid, transactionDB);
//
//        repository.update(uuid, BigDecimal.valueOf(500), "Transport", "Taxi");
//
//        TransactionDB updated = repository.getTransactions().get(uuid);
//        Assertions.assertThat(updated.getAmount()).isEqualTo(BigDecimal.valueOf(500));
//        Assertions.assertThat(updated.getCategory()).isEqualTo("Transport");
//        Assertions.assertThat(updated.getDescription()).isEqualTo("Taxi");
//    }
//
//    @Test
//    void update_doesNothingForNonExistentUuid() {
//        UUID uuid = UUID.randomUUID();
//
//        repository.update(uuid, BigDecimal.valueOf(500), "Transport", "Taxi");
//
//        Assertions.assertThat(repository.getTransactions()).doesNotContainKey(uuid);
//    }
//
//    @Test
//    void delete_removesTransaction() {
//        UUID uuid = UUID.randomUUID();
//        TransactionDB transactionDB = new TransactionDB(uuid, "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", LocalDate.now(), "Groceries");
//        repository.getTransactions().put(uuid, transactionDB);
//
//        repository.delete(uuid);
//
//        Assertions.assertThat(repository.getTransactions()).doesNotContainKey(uuid);
//    }
//
//    @Test
//    void findByUserEmailFilterDate_filtersByEmailAndDate() {
//        LocalDate date = LocalDate.now();
//        TransactionDB transactionDB1 = new TransactionDB(UUID.randomUUID(), "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", date, "Groceries");
//        TransactionDB transactionDB2 = new TransactionDB(UUID.randomUUID(), "user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", LocalDate.of(2025, 3, 11), "Monthly");
//        Transaction transaction1 = new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries");
//        repository.getTransactions().put(transactionDB1.getUuid(), transactionDB1);
//        repository.getTransactions().put(transactionDB2.getUuid(), transactionDB2);
//        Mockito.when(transactionDBMapper.toTransaction(transactionDB1)).thenReturn(Optional.of(transaction1));
//
//        List<Transaction> result = repository.findByUserEmailFilterDate("user@example.com", date);
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getDate()).isEqualTo(date);
//    }
//
//    @Test
//    void findByUserEmailFilterCategory_filtersByEmailAndCategory() {
//        TransactionDB transactionDB1 = new TransactionDB(UUID.randomUUID(), "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", LocalDate.now(), "Groceries");
//        TransactionDB transactionDB2 = new TransactionDB(UUID.randomUUID(), "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(100), "Transport", LocalDate.now(), "Taxi");
//        Transaction transaction1 = new Transaction("user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", "Groceries");
//        repository.getTransactions().put(transactionDB1.getUuid(), transactionDB1);
//        repository.getTransactions().put(transactionDB2.getUuid(), transactionDB2);
//        Mockito.when(transactionDBMapper.toTransaction(transactionDB1)).thenReturn(Optional.of(transaction1));
//
//        List<Transaction> result = repository.findByUserEmailFilterCategory("user@example.com", "Food");
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getCategory()).isEqualTo("Food");
//    }
//    @Test
//    void findByUserEmailFilterType_filtersByEmailAndType() {
//        TransactionDB transactionDB1 = new TransactionDB(UUID.randomUUID(), "user@example.com", TransactionType.EXPENSE, BigDecimal.valueOf(300), "Food", LocalDate.now(), "Groceries");
//        TransactionDB transactionDB2 = new TransactionDB(UUID.randomUUID(), "user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", LocalDate.now(), "Monthly");
//        Transaction transaction2 = new Transaction("user@example.com", TransactionType.INCOME, BigDecimal.valueOf(1000), "Salary", "Monthly");
//        repository.getTransactions().put(transactionDB1.getUuid(), transactionDB1);
//        repository.getTransactions().put(transactionDB2.getUuid(), transactionDB2);
//        Mockito.when(transactionDBMapper.toTransaction(transactionDB2)).thenReturn(Optional.of(transaction2));
//
//        List<Transaction> result = repository.findByUserEmailFilterType("user@example.com", TransactionType.INCOME);
//
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getType()).isEqualTo(TransactionType.INCOME);
//    }
//}
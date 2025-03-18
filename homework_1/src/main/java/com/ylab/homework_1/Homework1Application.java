package com.ylab.homework_1;

import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.infrastructure.repository.*;
import com.ylab.homework_1.infrastructure.service.*;
import com.ylab.homework_1.ui.console.ConsoleApp;
import com.ylab.homework_1.usecase.service.*;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.util.Properties;

public class Homework1Application {

    public static void main(String[] args) {
        try {
            Properties properties = loadProperties();
            PostgresDataSource.initDB(properties);
            NotificationService notificationService = new NotificationServiceImpl();
            UserService userService = new UserServiceImpl(new UserRepositoryImpl());
            BudgetService budgetService = new BudgetServiceImpl(new BudgetRepositoryImpl(), notificationService);
            GoalService goalService = new GoalServiceImpl(new GoalRepositoryImpl(), notificationService);
            TransactionService transactionService = new TransactionServiceImpl(new TransactionRepositoryImpl());
            StatisticsService statisticsService = new StatisticsServiceImpl(transactionService);
            AdministrationService administrationService = new AdministrationServiceImpl(userService, transactionService);
            ConsoleApp app = new ConsoleApp(userService, transactionService, budgetService, goalService, statisticsService, administrationService);

            app.start();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Properties loadProperties() throws Exception {
        Properties properties = new Properties();
        var inputStream = Homework1Application.class.getClassLoader().getResourceAsStream("application.properties");
        if (inputStream == null) {
            throw new IllegalStateException("Cannot find application.properties in classpath");
        }
        properties.load(inputStream);
        return properties;
    }

    private static void initializeLiquibase(Properties properties) throws Exception {
        System.out.println("Initializing Liquibase...");
        try (Connection connection = PostgresDataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            String changelogFile = properties.getProperty("liquibase.change-log");
            System.out.println("Using changelog: " + changelogFile);

            Liquibase liquibase = new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Liquibase migrations applied successfully");
        }
    }
}
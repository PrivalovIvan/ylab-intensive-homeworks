package com.ylab.homework_1.app;

import com.ylab.homework_1.Homework1Application;
import com.ylab.homework_1.infrastructure.datasource.PostgresDataSource;
import com.ylab.homework_1.infrastructure.repository.BudgetRepositoryImpl;
import com.ylab.homework_1.infrastructure.repository.GoalRepositoryImpl;
import com.ylab.homework_1.infrastructure.repository.TransactionRepositoryImpl;
import com.ylab.homework_1.infrastructure.repository.UserRepositoryImpl;
import com.ylab.homework_1.infrastructure.service.*;
import com.ylab.homework_1.ui.controller.*;
import com.ylab.homework_1.usecase.service.*;
import lombok.Getter;

import java.util.Properties;

@Getter
public class AppConfig {
    private static AppConfig instance;
    private final BudgetController budgetController;
    private final GoalController goalController;
    private final TransactionController transactionController;
    private final UserController userController;
    private final AdministrationController administrationController;
    private final StatisticController statisticController;

    public AppConfig() throws Exception {
        Properties properties = loadProperties();
        PostgresDataSource.initDB(properties);
        UserService userService = new UserServiceImpl(new UserRepositoryImpl());
        TransactionService transactionService = new TransactionServiceImpl(new TransactionRepositoryImpl());
        BudgetService budgetService = new BudgetServiceImpl(new BudgetRepositoryImpl(), new NotificationServiceImpl());
        GoalService goalService = new GoalServiceImpl(new GoalRepositoryImpl(), new NotificationServiceImpl());
        AdministrationService administrationService = new AdministrationServiceImpl(userService, transactionService);
        StatisticsService statisticsService = new StatisticsServiceImpl(transactionService);
        budgetController = new BudgetController(budgetService);
        goalController = new GoalController(goalService);
        userController = new UserController(userService);
        transactionController = new TransactionController(transactionService, goalService, budgetService);
        administrationController = new AdministrationController(administrationService);
        statisticController = new StatisticController(statisticsService);

    }

    public static AppConfig getInstance() throws Exception {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
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
}

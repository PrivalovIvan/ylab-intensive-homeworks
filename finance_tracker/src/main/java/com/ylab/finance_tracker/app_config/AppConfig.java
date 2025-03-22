package com.ylab.finance_tracker.app_config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.finance_tracker.Homework1Application;
import com.ylab.finance_tracker.infrastructure.datasource.PostgresDataSource;
import com.ylab.finance_tracker.infrastructure.repository.BudgetRepositoryImpl;
import com.ylab.finance_tracker.infrastructure.repository.GoalRepositoryImpl;
import com.ylab.finance_tracker.infrastructure.repository.TransactionRepositoryImpl;
import com.ylab.finance_tracker.infrastructure.repository.UserRepositoryImpl;
import com.ylab.finance_tracker.infrastructure.service.*;
import com.ylab.finance_tracker.ui.validator.ValidInputData;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.service.*;
import jakarta.inject.Singleton;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;

import java.util.Properties;

@Getter
@Singleton
public class AppConfig {
    private static AppConfig instance;
    private final UserService userService;
    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private final GoalService goalService;
    private final AdministrationService administrationService;
    private final StatisticsService statisticsService;
    private final ObjectMapper objectMapper;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private final ValidatorDTO validatorDTO;

    public AppConfig() throws Exception {
        Properties properties = loadProperties();
        PostgresDataSource.initDB(properties);
        userService = new UserServiceImpl(new UserRepositoryImpl());
        transactionService = new TransactionServiceImpl(new TransactionRepositoryImpl());
        budgetService = new BudgetServiceImpl(new BudgetRepositoryImpl(), new NotificationServiceImpl());
        goalService = new GoalServiceImpl(new GoalRepositoryImpl(), new NotificationServiceImpl());
        administrationService = new AdministrationServiceImpl(userService, transactionService);
        statisticsService = new StatisticsServiceImpl(transactionService);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        validatorDTO = new ValidInputData(validator, objectMapper);
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

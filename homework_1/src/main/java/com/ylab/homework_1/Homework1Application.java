package com.ylab.homework_1;


import com.ylab.homework_1.application.service.*;
import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.datasource.mapper.*;
import com.ylab.homework_1.datasource.repository.BudgetRepositoryImpl;
import com.ylab.homework_1.datasource.repository.GoalRepositoryImpl;
import com.ylab.homework_1.datasource.repository.TransactionRepositoryImpl;
import com.ylab.homework_1.domain.repository.BudgetRepository;
import com.ylab.homework_1.domain.repository.GoalRepository;
import com.ylab.homework_1.domain.repository.TransactionRepository;
import com.ylab.homework_1.domain.repository.UserRepository;
import com.ylab.homework_1.datasource.repository.UserRepositoryImpl;
import com.ylab.homework_1.domain.service.*;
import com.ylab.homework_1.ui.console.ConsoleApp;

public class Homework1Application {
    public static void main(String[] args) {
        UserDBMapper userDBMapper = new UserDBMapperImpl();
        UserRepository userRepository = new UserRepositoryImpl(userDBMapper);
        UserService userService = new UserServiceImpl(userRepository);

        TransactionDBMapper transactionDBMapper = new TransactionDBMapperImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(transactionDBMapper);

        NotificationService emailNotificationService = new EmailNotificationService();
        BudgetDBMapper budgetDBMapper = new BudgetDBMapperImpl();
        BudgetRepository budgetRepository = new BudgetRepositoryImpl(budgetDBMapper);
        BudgetService budgetService = new BudgetServiceImpl(budgetRepository, emailNotificationService);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository, budgetRepository, budgetService);

        GoalDBMapper goalDBMapper = new GoalDBMapperImpl();
        GoalRepository goalRepository = new GoalRepositoryImpl(goalDBMapper);
        GoalService goalService = new GoalServiceImpl(goalRepository);

        StatisticsService statisticsService = new StatisticsServiceImpl(transactionService);
        AdministrationService administrationService = new AdministrationServiceImpl(userService, transactionService);


        ConsoleApp app = new ConsoleApp
                (userService, transactionService, budgetService, goalService, statisticsService, administrationService);

        userService.register("admin", "admin", "admin", Role.ADMIN);
        userService.register("user", "user", "user", Role.USER);

        app.start();
    }
}

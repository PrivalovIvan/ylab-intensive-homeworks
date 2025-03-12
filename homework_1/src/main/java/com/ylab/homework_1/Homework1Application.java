package com.ylab.homework_1;

import com.ylab.homework_1.common.Role;
import com.ylab.homework_1.infrastructure.mapper.*;
import com.ylab.homework_1.infrastructure.repository.*;
import com.ylab.homework_1.infrastructure.service.*;
import com.ylab.homework_1.ui.console.ConsoleApp;
import com.ylab.homework_1.usecase.dto.UserDTO;
import com.ylab.homework_1.infrastructure.repository.GoalRepositoryImpl;
import com.ylab.homework_1.usecase.service.*;

import java.util.UUID;

public class Homework1Application {
    public static void main(String[] args) {
        NotificationService notificationService = new NotificationServiceImpl();
        UserService userService = new UserServiceImpl(new UserRepositoryImpl());
        BudgetService budgetService = new BudgetServiceImpl(new BudgetRepositoryImpl(), notificationService);
        GoalService goalService = new GoalServiceImpl(new GoalRepositoryImpl(), notificationService);
        TransactionService transactionService = new TransactionServiceImpl(new TransactionRepositoryImpl());
        StatisticsService statisticsService = new StatisticsServiceImpl(transactionService);
        AdministrationService administrationService = new AdministrationServiceImpl(userService, transactionService);
        ConsoleApp app = new ConsoleApp(userService, transactionService, budgetService, goalService, statisticsService, administrationService);

        UserDTO user = new UserDTO(UUID.randomUUID(), "user", "user@example.ru", "1234", Role.USER);
        userService.register(user);
        app.setCurrentUser(user);
        app.start();
    }
}

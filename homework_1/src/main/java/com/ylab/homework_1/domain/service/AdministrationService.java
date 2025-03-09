package com.ylab.homework_1.domain.service;

import com.ylab.homework_1.domain.model.Transaction;
import com.ylab.homework_1.domain.model.User;

import java.util.List;

public interface AdministrationService {
    List<User> findAllUsers();

    List<Transaction> findAllTransactionsOfUsers(String email);

    void deleteUser(String email);
}

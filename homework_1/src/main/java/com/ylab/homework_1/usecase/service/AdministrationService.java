package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.dto.UserDTO;

import java.util.List;

public interface AdministrationService {
    List<UserDTO> findAllUsers();

    List<TransactionDTO> findAllTransactionsOfUsers(String email);

    void deleteUser(String email);
}
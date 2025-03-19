package com.ylab.homework_1.usecase.service;

import com.ylab.homework_1.usecase.dto.TransactionDTO;
import com.ylab.homework_1.usecase.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface AdministrationService {
    List<UserDTO> findAllUsers() throws SQLException;

    List<TransactionDTO> findAllTransactionsOfUsers(String email) throws SQLException;

    void deleteUser(String email) throws SQLException;
}
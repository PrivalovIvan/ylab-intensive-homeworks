package com.ylab.finance_tracker.usecase.service;

import com.ylab.finance_tracker.usecase.dto.TransactionDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface AdministrationService {
    List<UserDTO> findAllUsers() throws SQLException;

    List<TransactionDTO> findAllTransactionsOfUsers(String email) throws SQLException;

    void deleteUser(String email) throws SQLException;
}
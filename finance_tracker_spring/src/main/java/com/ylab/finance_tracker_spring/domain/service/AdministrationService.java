package com.ylab.finance_tracker_spring.domain.service;

import com.ylab.finance_tracker_spring.dto.TransactionDTO;
import com.ylab.finance_tracker_spring.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface AdministrationService {
    List<UserDTO> findAllUsers() throws SQLException;

    List<TransactionDTO> findAllTransactionsOfUsers(String email) throws SQLException;

    void deleteUser(String email) throws SQLException;
}
package com.ylab.finance_tracker_spring_boot.domain.service;

import com.ylab.finance_tracker_spring_boot.dto.TransactionDTO;
import com.ylab.finance_tracker_spring_boot.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface AdministrationService {
    List<UserDTO> findAllUsers() throws SQLException;

    List<TransactionDTO> findAllTransactionsOfUsers(String email) throws SQLException;

    void deleteUser(String email) throws SQLException;
}
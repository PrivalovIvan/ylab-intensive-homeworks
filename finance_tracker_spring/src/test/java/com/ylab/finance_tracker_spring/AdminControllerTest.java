package com.ylab.finance_tracker_spring;

import com.ylab.finance_tracker_spring.common.Role;
import com.ylab.finance_tracker_spring.common.TransactionType;
import com.ylab.finance_tracker_spring.controller.AdminController;
import com.ylab.finance_tracker_spring.domain.service.AdministrationService;
import com.ylab.finance_tracker_spring.dto.TransactionDTO;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdministrationService administrationService;

    @InjectMocks
    private AdminController adminController;

    private UserDTO testUser;
    private TransactionDTO testTransaction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

        testUser = UserDTO.builder()
                .uuid(UUID.randomUUID())
                .name("Test User")
                .email("user@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        testTransaction = TransactionDTO.builder()
                .uuid(UUID.randomUUID())
                .email(testUser.getEmail())
                .type(TransactionType.EXPENSE)
                .amount(BigDecimal.valueOf(100.50))
                .category("Food")
                .description("Lunch at cafe")
                .build();
    }

    @Test
    void getAllUsers_WhenUsersExist_ReturnsUsersList() throws Exception {
        when(administrationService.findAllUsers())
                .thenReturn(List.of(testUser));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@example.com"))
                .andExpect(jsonPath("$[0].role").value("USER"));
    }

    @Test
    void getAllUsers_WhenNoUsers_ReturnsNoContent() throws Exception {
        when(administrationService.findAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllTransactions_WhenTransactionsExist_ReturnsTransactionsList() throws Exception {
        String userEmail = "user@example.com";
        when(administrationService.findAllTransactionsOfUsers(userEmail))
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(get("/admin/transactions")
                        .param("email", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(userEmail))
                .andExpect(jsonPath("$[0].amount").value(100.50));
    }

    @Test
    void getAllTransactions_WhenNoTransactions_ReturnsNoContent() throws Exception {
        String userEmail = "user@example.com";
        when(administrationService.findAllTransactionsOfUsers(userEmail)).thenReturn(List.of());

        mockMvc.perform(get("/admin/transactions")
                        .param("email", userEmail))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_WithValidEmail_ReturnsNoContent() throws Exception {
        String userEmail = "user@example.com";
        doNothing().when(administrationService).deleteUser(userEmail);

        mockMvc.perform(delete("/admin/users/delete")
                        .param("email", userEmail))
                .andExpect(status().isNoContent());

        verify(administrationService).deleteUser(userEmail);
    }
}

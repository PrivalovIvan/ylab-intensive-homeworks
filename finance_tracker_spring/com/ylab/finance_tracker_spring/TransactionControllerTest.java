import com.ylab.finance_tracker_spring.common.TransactionType;
import com.ylab.finance_tracker_spring.controller.TransactionController;
import com.ylab.finance_tracker_spring.domain.service.TransactionService;
import com.ylab.finance_tracker_spring.dto.TransactionDTO;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import com.ylab.finance_tracker_spring.security.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TransactionController transactionController;

    private UserDTO testUser;
    private TransactionDTO testTransaction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        testUser = new UserDTO();
        testUser.setEmail("test@example.com");

        testTransaction = TransactionDTO.builder()
                .uuid(UUID.randomUUID())
                .type(TransactionType.EXPENSE)
                .amount(BigDecimal.valueOf(100.50))
                .category("Food")
                .date(LocalDate.now())
                .description("Lunch at cafe")
                .build();
    }

    @Test
    void showAllTransactions_WhenAuthenticated_ReturnsTransactions() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(transactionService.findAllTransactionUser(testUser.email()))
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value("Food"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(100.50));
    }

    @Test
    void showAllTransactions_WhenNoTransactions_ReturnsNotFound() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(transactionService.findAllTransactionUser(testUser.email())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createTransaction_WithValidData_ReturnsOk() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);

        String requestBody = """
                {
                    "type": "EXPENSE",
                    "amount": 100.50,
                    "category": "Food",
                    "description": "Lunch at cafe"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(transactionService).createTransaction(any(TransactionDTO.class));
    }

    @Test
    void createTransaction_WithInvalidData_ReturnsBadRequest() throws Exception {
        String invalidRequestBody = """
                {
                    "type": null,
                    "amount": -100,
                    "category": "",
                    "description": ""
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void filterTransactionDate_WithValidDate_ReturnsTransactions() throws Exception {
        String testDate = "2023-01-01";
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(transactionService.findAllTransactionFilterByDate(testUser.email(), LocalDate.parse(testDate)))
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/filter/date")
                        .param("date", testDate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value("Food"));
    }

    @Test
    void filterTransactionCategory_WithValidCategory_ReturnsTransactions() throws Exception {
        String testCategory = "Food";
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(transactionService.findAllTransactionFilterByCategory(testUser.email(), testCategory))
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/filter/category")
                        .param("category", testCategory))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(testCategory));
    }

    @Test
    void filterTransactionType_WithValidType_ReturnsTransactions() throws Exception {
        String testType = "EXPENSE";
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(transactionService.findAllTransactionFilterByType(testUser.email(), TransactionType.valueOf(testType)))
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/filter/type")
                        .param("type", testType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value(testType));
    }

    @Test
    void deleteTransaction_WithValidId_ReturnsOk() throws Exception {
        UUID transactionId = UUID.randomUUID();
        when(authService.getCurrentUser()).thenReturn(testUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/delete")
                        .param("id", transactionId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(transactionService).deleteTransaction(testUser.email(), transactionId);
    }
}
import com.ylab.finance_tracker_spring.controller.GoalController;
import com.ylab.finance_tracker_spring.domain.service.GoalService;
import com.ylab.finance_tracker_spring.dto.GoalDTO;
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
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoalService goalService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private GoalController goalController;

    private UserDTO testUser;
    private GoalDTO testGoal;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(goalController).build();

        testUser = new UserDTO();
        testUser.setEmail("user@example.com");

        testGoal = GoalDTO.builder()
                .uuid(UUID.randomUUID())
                .email(testUser.email())
                .title("New Car")
                .targetAmount(BigDecimal.valueOf(25000))
                .savedAmount(BigDecimal.ZERO)
                .build();
    }

    @Test
    void getAllGoals_WhenGoalsExist_ReturnsGoals() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getUserGoals(testUser.email()))
                .thenReturn(List.of(testGoal));

        mockMvc.perform(MockMvcRequestBuilders.get("/goals"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("New Car"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetAmount").value(25000));
    }

    @Test
    void getAllGoals_WhenNoGoals_ReturnsNotFound() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getUserGoals(testUser.email())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/goals"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createGoal_WithValidData_ReturnsGoal() throws Exception {
        String requestBody = """
                {
                    "title": "New Car",
                    "targetAmount": 25000
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/goals/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(goalService).createGoal(any(GoalDTO.class));
    }

    @Test
    void createGoal_WithInvalidData_ReturnsBadRequest() throws Exception {
        String invalidRequestBody = """
                {
                    "title": "",
                    "targetAmount": -100
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/goals/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateGoal_WithExistingGoal_ReturnsUpdatedGoal() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getGoalByName(testUser.email(), "New Car"))
                .thenReturn(testGoal);

        mockMvc.perform(MockMvcRequestBuilders.put("/goals/update")
                        .param("name", "New Car")
                        .param("amount", "5000"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.savedAmount").value(5000));
    }

    @Test
    void updateGoal_WithNonExistingGoal_ReturnsNotFound() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getGoalByName(testUser.email(), "NonExistingGoal"))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/goals/update")
                        .param("name", "NonExistingGoal")
                        .param("amount", "5000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteGoal_WithExistingGoal_ReturnsDeletedGoal() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getGoalByName(testUser.email(), "New Car"))
                .thenReturn(testGoal);

        mockMvc.perform(MockMvcRequestBuilders.delete("/goals/delete")
                        .param("name", "New Car"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Car"));

        verify(goalService).deleteGoal(testUser.email(), "New Car");
    }

    @Test
    void deleteGoal_WithNonExistingGoal_ReturnsNotFound() throws Exception {
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(goalService.getGoalByName(testUser.email(), "NonExistingGoal"))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/goals/delete")
                        .param("name", "NonExistingGoal"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
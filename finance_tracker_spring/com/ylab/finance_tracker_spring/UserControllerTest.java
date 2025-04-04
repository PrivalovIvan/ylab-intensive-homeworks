import com.ylab.finance_tracker_spring.common.Role;
import com.ylab.finance_tracker_spring.controller.UserController;
import com.ylab.finance_tracker_spring.domain.service.UserService;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import com.ylab.finance_tracker_spring.security.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("GET /profile - успешное получение профиля")
    void showMyProfile_WhenAuthenticated_ReturnsUserProfile() throws Exception {
        UserDTO userDTO = new UserDTO(
                UUID.randomUUID(),
                "Test User",
                "test@example.com",
                "password",
                Role.USER
        );
        Mockito.when(authService.getCurrentUser()).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test User"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("GET /profile - неавторизованный доступ")
    void showMyProfile_WhenNotAuthenticated_ReturnsUnauthorized() throws Exception {
        Mockito.when(authService.getCurrentUser()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /profile/registration - успешная регистрация")
    void registry_WithValidData_ReturnsCreated() throws Exception {
        String requestBody = """
                {
                    "name": "New User",
                    "email": "new@example.com",
                    "password": "password123"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/profile/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Registration Successful"));

        verify(userService).register(any(UserDTO.class));
    }

    @Test
    @DisplayName("POST /profile/login - успешный вход")
    void login_WithValidCredentials_ReturnsOk() throws Exception {
        Mockito.when(authService.authenticate("user@example.com", "correctPassword")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/login")
                        .param("email", "user@example.com")
                        .param("password", "correctPassword"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Login Successful"));
    }

    @Test
    @DisplayName("POST /profile/login - неверные учетные данные")
    void login_WithInvalidCredentials_ReturnsUnauthorized() throws Exception {
        Mockito.when(authService.authenticate("user@example.com", "wrongPassword")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/login")
                        .param("email", "user@example.com")
                        .param("password", "wrongPassword"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Invalid email or password"));
    }
}
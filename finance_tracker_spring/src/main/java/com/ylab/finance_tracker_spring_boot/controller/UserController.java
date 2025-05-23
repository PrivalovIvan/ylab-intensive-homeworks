package com.ylab.finance_tracker_spring_boot.controller;

import com.ylab.finance_tracker_spring_boot.domain.service.UserService;
import com.ylab.finance_tracker_spring_boot.dto.UserDTO;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import com.ylab.finance_tracker_spring_boot.security_canon.JwtService;
import com.ylab.logging.annotation.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Validated
@Loggable
@Tag(name = "User Controller", description = "API для работы с пользователями")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final JwtService jwtService;

    @Operation(
            summary = "Получить профиль пользователя",
            description = "Возвращает данные авторизованного пользователя(личный кабинет)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Профиль получен",
                            content = @Content(
                                    schema = @Schema(implementation = UserDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Профиль не авторизован"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<UserDTO> showMyProfile() throws SQLException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Authorities: " + auth.getAuthorities());

        UserDTO user = authService.getCurrentUser();
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Регистрация нового пользователя в приложении",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Профиль зарегистрирован"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные пользователя"
                    )
            }

    )
    @PostMapping("/registration")
    public ResponseEntity<String> registry(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserDTO.class)
                    )
            )
            @Valid @RequestBody UserDTO userDTO)
            throws SQLException {
        userService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration Successful");
    }

    @Operation(
            summary = "Авторизация пользователя",
            description = "Аутентификация пользователя по email и паролю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    )
            }

    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password,
                                        HttpServletResponse response)
            throws SQLException {
        boolean authenticated = authService.authenticate(email, password);
        if (authenticated) {
            UserDTO user = authService.getCurrentUser();
            String token = jwtService.generateToken(user);
            response.addHeader("Authorization", "Bearer " + token);
            return ResponseEntity.ok("Login Successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to login");
    }
//    public ResponseEntity<String> login(
//            @Parameter(
//                    description = "email пользователя",
//                    required = true
//            )
//            @RequestParam String email,
//            @Parameter(
//                    description = "password пользователя",
//                    required = true
//            )
//            @RequestParam String password)
//            throws SQLException {
//        return authService.authenticate(email, password)
//                ? ResponseEntity.ok("Login Successful")
//                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//    }
}

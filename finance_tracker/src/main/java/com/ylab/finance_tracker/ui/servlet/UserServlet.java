package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.annotation.Loggable;
import com.ylab.finance_tracker.app_config.AppConfig;
import com.ylab.finance_tracker.common.Role;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@Loggable
@WebServlet("/profile/*")
public class UserServlet extends HttpServlet {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ValidatorDTO validatorDTO;

    public UserServlet() throws Exception {
        this(
                AppConfig.getInstance().getUserService(),
                AppConfig.getInstance().getObjectMapper(),
                AppConfig.getInstance().getValidatorDTO());
    }

    public UserServlet(UserService userService, ObjectMapper objectMapper, ValidatorDTO validatorDTO) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.validatorDTO = validatorDTO;
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = resp.getWriter()) {
            HttpSession session = req.getSession(false);
            UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

            if (currentUser != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(writer, userService.findByEmail(currentUser.getEmail()));
            }
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();
        try {
            switch (pathInfo) {
                case "/registration" -> {
                    String name = req.getParameter("name");
                    String email = req.getParameter("email");
                    String password = req.getParameter("password");
                    UserDTO userDTO = UserDTO.builder().name(name).email(email).password(password).role(Role.USER).build();
                    if (!validatorDTO.isValid(userDTO, resp)) {
                        return;
                    }
                    userService.register(userDTO);
                    writer.println("Registration successful!");
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                }
                case "/login" -> {
                    String email = req.getParameter("email");
                    String password = req.getParameter("password");
                    UserDTO currentUser = userService.login(email, password);
                    if (currentUser != null) {
                        HttpSession session = req.getSession(true);
                        session.setAttribute("currentUser", currentUser);
                        writer.println("Login successful!");
                        resp.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
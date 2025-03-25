package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.app_config.AppConfig;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.TransactionDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.AdministrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private final AdministrationService administrationService;
    private final ObjectMapper objectMapper;
    private final ValidatorDTO validatorDTO;

    //region constructor
    public AdminServlet() throws Exception {
        this(
                AppConfig.getInstance().getAdministrationService(),
                AppConfig.getInstance().getObjectMapper(),
                AppConfig.getInstance().getValidatorDTO()
        );
    }

    public AdminServlet(AdministrationService administrationService, ObjectMapper objectMapper, ValidatorDTO validatorDTO) {
        this.administrationService = administrationService;
        this.objectMapper = objectMapper;
        this.validatorDTO = validatorDTO;
    }
    //endregion

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        try {
            switch (pathInfo) {
                case "/users" -> {
                    List<UserDTO> users = administrationService.findAllUsers();
                    writer.write(objectMapper.writeValueAsString(users));
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
                case "/users/transactions" -> {
                    String email = req.getParameter("email");
                    List<TransactionDTO> transactions = administrationService.findAllTransactionsOfUsers(email);
                    if (transactions.isEmpty()) {
                        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        writer.write(objectMapper.writeValueAsString(transactions));
                        resp.setStatus(HttpServletResponse.SC_OK);
                    }
                }
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/users/delete")) {
            String email = req.getParameter("email");
            try {
                administrationService.deleteUser(email);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}

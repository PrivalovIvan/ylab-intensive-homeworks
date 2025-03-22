package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.annotation.Loggable;
import com.ylab.finance_tracker.app_config.AppConfig;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.BudgetDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.BudgetService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;

@Loggable
@WebServlet("/budgets/*")
public class BudgetServlet extends HttpServlet {
    private final BudgetService budgetService;
    private final ObjectMapper objectMapper;
    private final ValidatorDTO validatorDTO;


    public BudgetServlet() throws Exception {
        this(
                AppConfig.getInstance().getBudgetService(),
                AppConfig.getInstance().getObjectMapper(),
                AppConfig.getInstance().getValidatorDTO());
    }

    public BudgetServlet(BudgetService budgetService, ObjectMapper objectMapper, ValidatorDTO validatorDTO) {
        this.objectMapper = objectMapper;
        this.budgetService = budgetService;
        this.validatorDTO = validatorDTO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        PrintWriter writer = resp.getWriter();

        YearMonth yearMonth = YearMonth.parse(req.getParameter("yearMonth"));
        try {
            BudgetDTO budgetDTO = budgetService.getBudget(user.getEmail(), yearMonth).orElse(null);
            writer.println(objectMapper.writeValueAsString(budgetDTO));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("currentUser");

        try {
            YearMonth yearMonth = YearMonth.parse(req.getParameter("yearMonth"));
            BigDecimal amountLimit = BigDecimal.valueOf(Double.parseDouble(req.getParameter("amountLimit")));
            BudgetDTO budgetDTO = BudgetDTO.builder()
                    .email(user.getEmail())
                    .yearMonth(yearMonth)
                    .budget(amountLimit)
                    .spent(BigDecimal.ZERO)
                    .build();
            if (!validatorDTO.isValid(budgetDTO, resp)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            budgetService.createBudget(budgetDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("currentUser");

        try {
            YearMonth yearMonth = YearMonth.parse(req.getParameter("yearMonth"));
            BigDecimal expense = BigDecimal.valueOf(Double.parseDouble(req.getParameter("expense")));
            budgetService.addExpense(user.getEmail(), yearMonth, expense);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.annotation.Loggable;
import com.ylab.finance_tracker.app_config.AppConfig;
import com.ylab.finance_tracker.common.TransactionType;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.TransactionDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.BudgetService;
import com.ylab.finance_tracker.usecase.service.TransactionService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

@Loggable
@WebServlet("/transactions/*")
public class TransactionServlet extends HttpServlet {
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;
    private final BudgetService budgetService;
    private final ValidatorDTO validatorDTO;

    public TransactionServlet() throws Exception {
        this(
                AppConfig.getInstance().getTransactionService(),
                AppConfig.getInstance().getObjectMapper(),
                AppConfig.getInstance().getBudgetService(),
                AppConfig.getInstance().getValidatorDTO());
    }

    public TransactionServlet(TransactionService transactionService,
                              ObjectMapper objectMapper,
                              BudgetService budgetService,
                              ValidatorDTO validatorDTO) {
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
        this.budgetService = budgetService;
        this.validatorDTO = validatorDTO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");

        try {
            writer.write(objectMapper.writeValueAsString(transactionService.findAllTransactionUser(currentUser.getEmail())));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
        try {
            String typeStr = req.getParameter("type");
            TransactionType transactionType = typeStr != null ? TransactionType.valueOf(typeStr.toUpperCase()) : null;
            String amountStr = req.getParameter("amount");
            BigDecimal amount = amountStr != null ? new BigDecimal(amountStr) : null;
            String category = req.getParameter("category");
            String description = req.getParameter("description");
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .email(currentUser.getEmail())
                    .type(transactionType)
                    .amount(amount)
                    .category(category)
                    .date(LocalDate.now())
                    .description(description)
                    .build();
            if (!validatorDTO.isValid(transactionDTO, resp)) {
                return;
            }
            transactionService.createTransaction(transactionDTO);
            assert transactionType != null;
            if (transactionType.equals(TransactionType.EXPENSE)) {
                budgetService.processExpense(transactionDTO);
            }
            writer.write(objectMapper.writeValueAsString(transactionDTO));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");

        try {
            UUID idTransaction = UUID.fromString(req.getParameter("id"));
            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(req.getParameter("amount")));
            String category = req.getParameter("category");
            String description = req.getParameter("description");
            transactionService.updateAmount(currentUser.getEmail(), idTransaction, amount);
            transactionService.updateCategory(currentUser.getEmail(), idTransaction, category);
            transactionService.updateDescription(currentUser.getEmail(), idTransaction, description);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");

        try {
            UUID idTransaction = UUID.fromString(req.getParameter("id"));
            transactionService.deleteTransaction(currentUser.getEmail(), idTransaction);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}

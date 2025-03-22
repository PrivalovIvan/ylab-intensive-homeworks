package com.ylab.finance_tracker.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.annotation.Loggable;
import com.ylab.finance_tracker.app_config.AppConfig;
import com.ylab.finance_tracker.ui.validator.ValidatorDTO;
import com.ylab.finance_tracker.usecase.dto.GoalDTO;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.GoalService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Loggable
@WebServlet("/goals/*")
public class GoalServlet extends HttpServlet {
    private final GoalService goalService;
    private final ObjectMapper objectMapper;
    private final ValidatorDTO validatorDTO;

    public GoalServlet() throws Exception {
        this(
                AppConfig.getInstance().getGoalService(),
                AppConfig.getInstance().getObjectMapper(),
                AppConfig.getInstance().getValidatorDTO()
        );
    }

    public GoalServlet(GoalService goalService, ObjectMapper objectMapper, ValidatorDTO validatorDTO) {
        this.goalService = goalService;
        this.objectMapper = objectMapper;
        this.validatorDTO = validatorDTO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        try {
            List<GoalDTO> goals = goalService.getUserGoals(user.getEmail());
            if (!goals.isEmpty()) {
                writer.write(objectMapper.writeValueAsString(goals));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
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
        PrintWriter writer = resp.getWriter();

        try {
            String nameGoal = req.getParameter("nameGoal");
            String amountStr = req.getParameter("amount");
            BigDecimal amount = amountStr != null ? new BigDecimal(amountStr) : null;
            GoalDTO goalDTO = GoalDTO.builder()
                    .email(user.getEmail())
                    .title(nameGoal)
                    .targetAmount(amount)
                    .savedAmount(BigDecimal.ZERO)
                    .build();
            if (!validatorDTO.isValid(goalDTO, resp)) {
                return;
            }
            goalService.createGoal(goalDTO);
            writer.write("Goal created");
            writer.write(objectMapper.writeValueAsString(goalDTO));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        try {
            String nameGoal = req.getParameter("nameGoal");
            BigDecimal amount = new BigDecimal(req.getParameter("amount"));
            GoalDTO existingGoal = goalService.getGoalByName(user.getEmail(), nameGoal);
            BigDecimal newSavedAmount = existingGoal.getSavedAmount().add(amount);
            GoalDTO updatedGoal = GoalDTO.builder()
                    .email(user.getEmail())
                    .title(nameGoal)
                    .targetAmount(existingGoal.getTargetAmount())
                    .savedAmount(newSavedAmount)
                    .build();
            if (!validatorDTO.isValid(updatedGoal, resp)) {
                return;
            }
            goalService.updateGoal(updatedGoal);
            writer.write("Goal updated");
            writer.write(objectMapper.writeValueAsString(updatedGoal));
            goalService.checkAndNotify(user.getEmail(), nameGoal);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("currentUser");

        String nameGoal = req.getParameter("nameGoal");
        try {
            goalService.deleteGoal(user.getEmail(), nameGoal);
            writer.write("Goal deleted");
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

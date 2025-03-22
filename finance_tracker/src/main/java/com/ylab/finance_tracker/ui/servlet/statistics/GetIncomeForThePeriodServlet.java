package com.ylab.finance_tracker.ui.servlet.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.app_config.AppConfig;
import com.ylab.finance_tracker.common.TransactionType;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import com.ylab.finance_tracker.usecase.service.StatisticsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/statistics/income")
public class GetIncomeForThePeriodServlet extends HttpServlet {
    private final StatisticsService statisticsService;
    private final ObjectMapper objectMapper;

    public GetIncomeForThePeriodServlet() throws Exception {
        this(
                AppConfig.getInstance().getStatisticsService(),
                AppConfig.getInstance().getObjectMapper()
        );
    }

    public GetIncomeForThePeriodServlet(StatisticsService statisticsService, ObjectMapper objectMapper) {
        this.statisticsService = statisticsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("currentUser");

        try {
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);
            writer.write(objectMapper.writeValueAsString(statisticsService.getTotal(user.getEmail(), fromDate, toDate, TransactionType.INCOME)));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (DateTimeParseException e) {
            writer.write("Invalid 'from' or 'to' date format");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

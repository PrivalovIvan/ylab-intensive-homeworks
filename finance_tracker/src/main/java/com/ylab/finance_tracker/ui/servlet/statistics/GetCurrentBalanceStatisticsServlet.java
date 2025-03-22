package com.ylab.finance_tracker.ui.servlet.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.finance_tracker.app_config.AppConfig;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/statistics/budget")
public class GetCurrentBalanceStatisticsServlet extends HttpServlet {
    private final StatisticsService statisticsService;
    private final ObjectMapper objectMapper;

    public GetCurrentBalanceStatisticsServlet() throws Exception {
        this(
                AppConfig.getInstance().getStatisticsService(),
                AppConfig.getInstance().getObjectMapper()
        );
    }

    public GetCurrentBalanceStatisticsServlet(StatisticsService statisticsService, ObjectMapper objectMapper) {
        this.statisticsService = statisticsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession(false);
        try {
            UserDTO user = (UserDTO) session.getAttribute("currentUser");

            BigDecimal balance = statisticsService.getCurrentBalance(user.getEmail());
            Map<String, BigDecimal> balanceJson = Map.of("balance", balance);
            writer.write(objectMapper.writeValueAsString(balanceJson));
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

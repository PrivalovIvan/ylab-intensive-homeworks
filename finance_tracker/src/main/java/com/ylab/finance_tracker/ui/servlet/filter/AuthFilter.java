package com.ylab.finance_tracker.ui.servlet.filter;

import com.ylab.finance_tracker.common.Role;
import com.ylab.finance_tracker.usecase.dto.UserDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/profile/*", "/transactions/*", "/goals/*", "/budgets/*", "/admin/*", "/statistics/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();


        if ("/login".equals(pathInfo) || "/registration".equals(pathInfo) || "POST".equals(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Login required");
            return;
        }

        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        if (user.getRole() == Role.ADMIN) {
            chain.doFilter(request, response);
            return;
        }

        if (servletPath.startsWith("/admin")) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied, only Admin");
            return;
        }

        chain.doFilter(request, response);
    }
}

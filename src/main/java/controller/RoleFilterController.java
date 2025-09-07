package controller;

import entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class RoleFilterController implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // bypass
        if (path.startsWith("/waiting") || path.equals("/login")) {
            chain.doFilter(request, response); return;
        }

        User u = (User) req.getSession().getAttribute("account");
        if (u == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }

        int role = u.getRole();
        // guard by prefix
        if (path.startsWith("/admin/") && role != 3) { resp.sendError(403); return; }
        if (path.startsWith("/mgr/") && role != 2) { resp.sendError(403); return; }
        if (path.startsWith("/user/") && role != 1) { resp.sendError(403); return; }
 chain.doFilter(request, response);

    }
}

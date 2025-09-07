package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import entity.User;

import java.io.IOException;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/waiting")

public class WaitingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        HttpSession session= req.getSession();
        if(session != null && session.getAttribute("account") != null) {
            User u=(User) session.getAttribute("account");
            req.setAttribute("username", u.getUsername());
            if(u.getRole()==1) {
                resp.sendRedirect(req.getContextPath()+"/user/home");
            }else if(u.getRole()==2) {
                resp.sendRedirect(req.getContextPath()+"/mgr/home");
            }else {
                resp.sendRedirect(req.getContextPath()+"/admin/home");
            }
        }else {
            resp.sendRedirect(req.getContextPath()+"/login");
        }
    }
}


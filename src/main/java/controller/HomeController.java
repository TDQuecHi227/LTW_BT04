package controller;

import dao.CategoryDao;
import dao.impl.CategoryDaoImpl;
import entity.Category;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/home", "/user/home", "/mgr/home"})
public class HomeController extends HttpServlet {
    private final CategoryDao categoryDao = new CategoryDaoImpl(Category.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u=(User) req.getSession().getAttribute("account");
        String path =  req.getServletPath();
        List<Category> categoryList;
        if(path.equals("/mgr/home"))
        {
            categoryList = categoryDao.findByUserId(u.getUserId());
            req.setAttribute("nameRole", "Manager");
        }
        else{
            categoryList = categoryDao.findAll();
            if(path.equals("/admin/home"))
            {
                req.setAttribute("nameRole", "Admin");
            }
            else{
                req.setAttribute("nameRole", "User");
            }
        }
        req.setAttribute("categoryList", categoryList);
        req.getRequestDispatcher("/views/list.jsp").forward(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect("/login");
    }
}

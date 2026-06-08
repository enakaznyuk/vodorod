package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.SessionUtil;

import java.io.IOException;

@WebServlet("")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}

package servlets;

import dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthService;
import util.SessionUtil;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username != null) {
            username = username.trim();
        }

        UserDto user = authService.login(username, password);
        if (user == null) {
            req.setAttribute("error", "Неверный логин или пароль");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        SessionUtil.login(req, user);
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}

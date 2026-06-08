package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthService;
import util.SessionUtil;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");

        if (password == null || !password.equals(confirm)) {
            req.setAttribute("error", "Пароли не совпадают");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        if (!authService.register(username, password)) {
            req.setAttribute("error", "Не удалось зарегистрироваться. Возможно, имя уже занято.");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }
}

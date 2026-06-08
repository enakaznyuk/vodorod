package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import dto.UserDto;
import service.UserService;
import util.SessionUtil;
import java.io.IOException;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");

        if (id != null) {
            req.setAttribute("user", userService.findById(
                    Integer.parseInt(id)));
        }
        req.setAttribute("users", userService.findAll());
        req.getRequestDispatcher(
                        "/WEB-INF/views/users.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("create".equals(action)) {
            UserDto dto = new UserDto();
            dto.setUsername(req.getParameter("username"));
            dto.setRole(normalizeRole(req.getParameter("role")));
            dto.setPassword(req.getParameter("password"));
            userService.save(dto);
        } else if ("delete".equals(action)) {
            userService.delete(Integer.parseInt(req.getParameter("id")));
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            UserDto user = userService.findById(id);
            if (user != null) {
                user.setUsername(req.getParameter("username"));
                user.setPassword(req.getParameter("password"));
                user.setRole(normalizeRole(req.getParameter("role")));
                userService.update(id, user);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/users");
    }

    private String normalizeRole(String role) {
        if (SessionUtil.ROLE_ADMIN.equals(role)) {
            return SessionUtil.ROLE_ADMIN;
        }
        return SessionUtil.ROLE_CLIENT;
    }
}

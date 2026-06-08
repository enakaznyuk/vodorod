package servlets;

import dto.OrderDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.OrderService;
import service.ProductService;
import service.UserService;
import util.SessionUtil;

import java.io.IOException;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        boolean admin = SessionUtil.isAdmin(req);
        int currentUserId = SessionUtil.getUserId(req);
        String id = req.getParameter("id");

        if (id != null) {
            OrderDto order = orderService.findById(Integer.parseInt(id));
            if (order != null && (admin || order.getUserId() == currentUserId)) {
                req.setAttribute("order", order);
            }
        }

        if (admin) {
            req.setAttribute("orders", orderService.findAll());
            req.setAttribute("users", userService.findAll());
        } else {
            req.setAttribute("orders", orderService.findByUserId(currentUserId));
        }

        req.setAttribute("products", productService.findAll());
        req.setAttribute("admin", admin);
        req.setAttribute("currentUserId", currentUserId);

        req.getRequestDispatcher("/WEB-INF/views/orders.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        boolean admin = SessionUtil.isAdmin(req);
        int userId = admin
                ? Integer.parseInt(req.getParameter("userId"))
                : SessionUtil.getUserId(req);

        String action = req.getParameter("action");
        int[] productIds = parseIntArray(req.getParameterValues("productId"));
        int[] quantities = parseIntArray(req.getParameterValues("quantity"));

        if ("create".equals(action)) {
            orderService.save(userId, productIds, quantities);
        } else if ("update".equals(action)) {
            int orderId = Integer.parseInt(req.getParameter("id"));
            if (!admin) {
                OrderDto order = orderService.findById(orderId);
                if (order == null || order.getUserId() != SessionUtil.getUserId(req)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
            orderService.update(orderId, userId, productIds, quantities);
        }

        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    private int[] parseIntArray(String[] values) {
        if (values == null) {
            return new int[0];
        }
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Integer.parseInt(values[i]);
        }
        return result;
    }
}

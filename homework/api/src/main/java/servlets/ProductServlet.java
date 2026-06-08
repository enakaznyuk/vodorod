package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import dto.ProductDto;
import service.ProductService;
import util.SessionUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");

        if (id != null && SessionUtil.isAdmin(req)) {
            req.setAttribute("product", productService.findById(
                    Integer.parseInt(id)));
        }
        req.setAttribute("products", productService.findAll());
        req.setAttribute("admin", SessionUtil.isAdmin(req));
        req.getRequestDispatcher("/WEB-INF/views/products.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!SessionUtil.isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ только для ADMIN");
            return;
        }

        String action = req.getParameter("action");

        if ("create".equals(action)) {
            ProductDto dto = new ProductDto();
            dto.setName(req.getParameter("name"));
            dto.setDescription(req.getParameter("description"));
            dto.setPrice(new BigDecimal(req.getParameter("price")));
            productService.save(dto);
        } else if ("delete".equals(action)) {
            productService.delete(Integer.parseInt(req.getParameter("id")));
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            ProductDto product = new ProductDto();
            product.setName(req.getParameter("name"));
            product.setDescription(req.getParameter("description"));
            product.setPrice(new BigDecimal(req.getParameter("price")));
            productService.update(id, product);
        }

        resp.sendRedirect(req.getContextPath() + "/products");
    }
}

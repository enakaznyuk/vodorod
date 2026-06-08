package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.SessionUtil;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();

        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        if (!SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (path.startsWith("/users") && !SessionUtil.isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ только для ADMIN");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return "/login".equals(path)
                || "/register".equals(path)
                || "/logout".equals(path);
    }
}

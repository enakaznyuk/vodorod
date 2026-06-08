package util;

import dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionUtil {

    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String ROLE = "role";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_CLIENT = "CLIENT";

    private SessionUtil() {}

    public static void login(HttpServletRequest req, UserDto user) {
        HttpSession session = req.getSession(true);
        session.setAttribute(USER_ID, user.getId());
        session.setAttribute(USERNAME, user.getUsername());
        session.setAttribute(ROLE, user.getRole());
    }

    public static void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute(USER_ID) != null;
    }

    public static boolean isAdmin(HttpServletRequest req) {
        return ROLE_ADMIN.equals(getRole(req));
    }

    public static int getUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(USER_ID) == null) {
            return 0;
        }
        return (int) session.getAttribute(USER_ID);
    }

    public static String getRole(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(ROLE);
    }
}

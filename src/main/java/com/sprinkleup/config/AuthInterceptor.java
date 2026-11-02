package com.sprinkleup.config;

import com.sprinkleup.exception.ForbiddenException;
import com.sprinkleup.model.User;
import com.sprinkleup.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Runs before every page. It works out who is signed in (shared with the
 * pages as "currentUser") and keeps people out of pages that aren't theirs:
 * /admin is for staff only; /account and /custom-orders are for customers.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String SESSION_USER_ID = "userId";
    public static final String CURRENT_USER = "currentUser";

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = loadUser(request);
        request.setAttribute(CURRENT_USER, user);

        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (path.startsWith("/admin")) {
            if (user == null) {
                return sendToSignIn(request, response, path);
            }
            if (!user.isAdmin()) {
                throw new ForbiddenException("The bakery admin pages are for SprinkleUp staff only.");
            }
        }
        if (path.startsWith("/account") || path.startsWith("/custom-orders")) {
            if (user == null) {
                return sendToSignIn(request, response, path);
            }
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin");
                return false;
            }
        }
        return true;
    }

    private User loadUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute(SESSION_USER_ID) instanceof Long id)) {
            return null;
        }
        User user = authService.findUser(id).orElse(null);
        if (user == null) {
            session.invalidate(); // the account was deleted
        }
        return user;
    }

    private boolean sendToSignIn(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        String next = "GET".equals(request.getMethod()) ? path + (request.getQueryString() == null ? "" : "?" + request.getQueryString()) : path;
        response.sendRedirect(request.getContextPath() + "/login?next=" + URLEncoder.encode(next, StandardCharsets.UTF_8));
        return false;
    }
}

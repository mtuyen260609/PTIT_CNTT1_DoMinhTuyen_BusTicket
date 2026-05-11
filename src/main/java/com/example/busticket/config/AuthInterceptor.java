package com.example.busticket.config;

import com.example.busticket.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String role = (String) request.getSession().getAttribute("role");

        if (isPublicPath(path)) {
            return true;
        }

        if (path.startsWith("/admin")) {
            return requireRole(role, response, Role.ADMIN);
        }

        if (path.startsWith("/staff")) {
            return requireAnyRole(role, response, Role.STAFF, Role.ADMIN);
        }

        if (path.startsWith("/profile")) {
            return requireLogin(role, response);
        }

        return requireLogin(role, response);
    }

    private boolean isPublicPath(String path) {
        return path.equals("/login")
                || path.equals("/register")
                || path.equals("/error")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/webjars/")
                || path.equals("/app.css")
                || path.equals("/favicon.ico");
    }

    private boolean requireLogin(String role, HttpServletResponse response) throws Exception {
        if (role == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    private boolean requireRole(String currentRole, HttpServletResponse response, Role requiredRole) throws Exception {
        if (!requireLogin(currentRole, response)) {
            return false;
        }
        if (!requiredRole.name().equals(currentRole)) {
            response.sendRedirect("/access-denied");
            return false;
        }
        return true;
    }

    private boolean requireAnyRole(String currentRole, HttpServletResponse response, Role... roles) throws Exception {
        if (!requireLogin(currentRole, response)) {
            return false;
        }

        for (Role role : roles) {
            if (role.name().equals(currentRole)) {
                return true;
            }
        }

        response.sendRedirect("/access-denied");
        return false;
    }
}

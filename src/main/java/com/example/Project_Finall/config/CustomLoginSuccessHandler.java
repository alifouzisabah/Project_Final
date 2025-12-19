package com.example.Project_Finall.config;

import com.example.Project_Finall.modell.Status;
import com.example.Project_Finall.modell.Users;
import com.example.Project_Finall.service.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UsersService usersService;

    @Autowired
    public CustomLoginSuccessHandler( @Lazy UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var roles = authentication.getAuthorities();
        String username = authentication.getName();
        Users user = usersService.findByUsername(username);
        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_MANAGER"))) {
            response.sendRedirect("/manager/dashboard");
        } else if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_TEACHER"))) {
            response.sendRedirect("/teacher/dashboard");
        } else if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_STUDENT"))) {
            response.sendRedirect("/student/dashboard");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}

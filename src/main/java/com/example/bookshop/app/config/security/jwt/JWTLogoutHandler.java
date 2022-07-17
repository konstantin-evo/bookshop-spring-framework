package com.example.bookshop.app.config.security.jwt;

import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.services.CookieUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class JWTLogoutHandler implements LogoutHandler {

    private final static String JWT_COOKIE_NAME = "token";

    private final UserRegisterService userRegisterService;

    public JWTLogoutHandler(@Lazy UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        SecurityContextHolder.clearContext();
        String token = CookieUtil.getValue(request, JWT_COOKIE_NAME);
        if (token != null) {
            userRegisterService.logout(token);
        }
    }
}

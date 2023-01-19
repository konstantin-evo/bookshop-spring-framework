package com.example.bookshop.app.aspect;

import com.example.bookshop.app.config.security.BookshopUserDetails;
import com.example.bookshop.app.config.security.BookshopUserDetailsService;
import com.example.bookshop.app.config.security.jwt.JWTUtil;
import com.example.bookshop.app.config.security.oauth.CustomOAuth2User;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * Aspect for logging authentication
 */
@Component
@Aspect
@RequiredArgsConstructor
@Log4j2
public class AuthLogAspect {

    private final BookshopUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private String username;

    @Pointcut("execution(public * com.example.bookshop.web.controllers.user.UserAuthController.handleLogin(..)) && args(payload, ..)")
    public void loginByPassword(ContactConfirmationPayload payload) {
    }

    @Pointcut("execution(public * com.example.bookshop.app.config.security.oauth.OauthSuccessHandler.onAuthenticationSuccess(..)) && args(.., authentication)")
    public void loginByOauth(Authentication authentication) {
    }

    @Pointcut("execution(public * com.example.bookshop.web.controllers.user.UserAuthController.handleLogin(..))")
    public void callAfterLoginSuccess() {
    }

    @Before(value = "loginByPassword(payload)", argNames = "payload")
    public void loginStart(ContactConfirmationPayload payload) {
        BookshopUserDetails userDetails = (BookshopUserDetails) userDetailsService.loadUserByUsername(payload.getContact());
        log.info("The user (email = {}) is trying to login by login/password", userDetails.getUsername());
    }

    @Before(value = "loginByOauth(authentication)", argNames = "authentication")
    public void loginStart(Authentication authentication) {

        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        username = oauthUser.getEmail();

        log.info("The user (email = {}) is trying to login by OAuth2", username);

        try {
            userDetailsService.loadUserByUsername(oauthUser.getEmail());
            log.info("The user (email = {}) logged in successfully.", username);
        } catch (UsernameNotFoundException e) {
            log.info("The user with the {} email is not in the database. The new user has been created and  logged in successfully.", oauthUser.getEmail());
        }
    }

    @AfterReturning(pointcut = "callAfterLoginSuccess()", returning = "jwt")
    public void loginSuccess(ContactConfirmationResponse jwt) {
        username = jwtUtil.extractUsername(jwt.getResult());
        log.info("The user (email = {}) logged in successfully. JWT has been generated: {}",
                username, jwt.getResult());
    }

    @Before("execution(public * com.example.bookshop.app.config.security.jwt.JWTLogoutHandler.logout(..))")
    public void beforeLogout() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("User (email = {}) successfully logged out.", SecurityContextHolder.getContext().getAuthentication().getName());
        }
    }
}

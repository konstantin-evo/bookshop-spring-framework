package com.example.bookshop.app.config.security.oauth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.bookshop.app.config.security.BookshopUserDetails;
import com.example.bookshop.app.config.security.UserDetailsService;
import com.example.bookshop.app.services.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class OauthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRegisterService userRegisterService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public OauthSuccessHandler(@Lazy UserRegisterService userRegisterService,
                               UserDetailsService userDetailsService) {
        this.userRegisterService = userRegisterService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        BookshopUserDetails userDetails;
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        try {
            userDetails = (BookshopUserDetails) userDetailsService.loadUserByUsername(oauthUser.getEmail());
        } catch (UsernameNotFoundException e) {
            userDetails = userRegisterService.registerNewUser(oauthUser);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        response.sendRedirect("/home");
    }
}

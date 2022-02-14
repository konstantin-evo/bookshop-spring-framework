package com.example.bookshop.app.config.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauthUser;

    public CustomOAuth2User(OAuth2User oauthUser) {
        this.oauthUser = oauthUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauthUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauthUser.getAuthorities();
    }

    @Override
    public String getName() {
        return oauthUser.getAttribute("email");
    }

    public String getEmail() {
        return oauthUser.getAttribute("email");
    }
}

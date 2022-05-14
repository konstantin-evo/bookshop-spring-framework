package com.example.bookshop.app.config.security;

import com.example.bookshop.app.config.security.jwt.JWTLogoutHandler;
import com.example.bookshop.app.config.security.jwt.JWTRequestFilter;
import com.example.bookshop.app.config.security.oauth.CustomOAuth2UserService;
import com.example.bookshop.app.config.security.oauth.OauthSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService oauthUserService;
    private final OauthSuccessHandler oauthSuccessHandler;
    private final JWTLogoutHandler jwtLogoutHandler;
    private final JWTRequestFilter filter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          CustomOAuth2UserService oauthUserService,
                          OauthSuccessHandler oauthSuccessHandler,
                          JWTLogoutHandler jwtLogoutHandler,
                          JWTRequestFilter filter) {
        this.userDetailsService = userDetailsService;
        this.oauthUserService = oauthUserService;
        this.oauthSuccessHandler = oauthSuccessHandler;
        this.jwtLogoutHandler = jwtLogoutHandler;
        this.filter = filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/home", "/profile", "/archive").authenticated()
                .antMatchers("/**").permitAll()
                .and()
                    .formLogin()
                        .loginPage("/signin")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/")
                .and()
                    .logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("token")
                        .addLogoutHandler(jwtLogoutHandler)
                .and()
                    .oauth2Login()
                        .defaultSuccessUrl("/home")
                        .userInfoEndpoint()
                        .userService(oauthUserService)
                .and()
                    .successHandler(oauthSuccessHandler);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

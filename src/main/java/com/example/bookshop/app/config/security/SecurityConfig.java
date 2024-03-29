package com.example.bookshop.app.config.security;

import com.example.bookshop.app.config.security.jwt.JWTLogoutHandler;
import com.example.bookshop.app.config.security.jwt.JWTRequestFilter;
import com.example.bookshop.app.config.security.oauth.CustomOAuth2UserService;
import com.example.bookshop.app.config.security.oauth.OauthSuccessHandler;
import com.example.bookshop.app.model.entity.enumuration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BookshopUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oauthUserService;
    private final OauthSuccessHandler oauthSuccessHandler;
    private final JWTLogoutHandler jwtLogoutHandler;
    private final JWTRequestFilter filter;

    private static final String HOME_PAGE = "/home";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HOME_PAGE, "/profile", "/archive", "books/order", "/books/viewed").authenticated()
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.getValue())
                .antMatchers(HttpMethod.POST,"api/books", "api/book-to-user").hasAuthority(Role.ADMIN.getValue())
                .antMatchers(HttpMethod.PATCH,"api/books/**", "api/authors/**", "api/users/**", "api/book-to-user").hasAuthority(Role.ADMIN.getValue())
                .antMatchers(HttpMethod.DELETE,"api/books/**", "api/reviews/**").hasAuthority(Role.ADMIN.getValue())
                .antMatchers("/**").permitAll()
                .and()
                  .exceptionHandling().accessDeniedPage("/")
                .and()
                    .formLogin()
                        .loginPage("/signin")
                        .defaultSuccessUrl(HOME_PAGE)
                        .failureUrl("/")
                .and()
                    .logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("token")
                        .addLogoutHandler(jwtLogoutHandler)
                .and()
                    .oauth2Login()
                        .defaultSuccessUrl(HOME_PAGE)
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

package com.example.bookshop.app.services;

import com.example.bookshop.app.config.security.UserDetailsService;
import com.example.bookshop.app.config.security.jwt.JWTUtil;
import com.example.bookshop.app.config.security.oauth.CustomOAuth2User;
import com.example.bookshop.app.model.dao.JwtBlockListRepository;
import com.example.bookshop.app.model.entity.JwtBlockList;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.app.config.security.UserDetails;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.app.model.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;

@Service
public class UserRegisterService {

    private static final String ANONYMOUS_USER = "anonymousUser";

    private final UserRepository userRepo;
    private final JwtBlockListRepository jwtBlockListRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    public UserRegisterService(UserRepository userRepo,
                               JwtBlockListRepository jwtBlockListRepo,
                               PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager,
                               UserDetailsService userDetailsService,
                               JWTUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtBlockListRepo = jwtBlockListRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Transient
    public void registerNewUser(RegistrationFormDto registrationFormDto) {

        if (userRepo.findUserByEmail(registrationFormDto.getEmail()) == null) {
            User user = new User();
            user.setName(registrationFormDto.getName());
            user.setEmail(registrationFormDto.getEmail());
            user.setPhone(registrationFormDto.getPhone());
            user.setPassword(passwordEncoder.encode(registrationFormDto.getPassword()));
            userRepo.save(user);
        }
    }

    @Transient
    public UserDetails registerNewUser(CustomOAuth2User oAuth2User) {
        User user = new User(
                (String) oAuth2User.getAttributes().get("name"),
                (String) oAuth2User.getAttributes().get("email"));
        userRepo.save(user);
        return new UserDetails(user);
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ContactConfirmationResponse("true");
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (payload.getContact(), payload.getCode()));

        UserDetails userDetails = (UserDetails)
                userDetailsService.loadUserByUsername(payload.getContact());

        String jwtToken = jwtUtil.generateToken(userDetails);

        return new ContactConfirmationResponse(jwtToken);
    }

    public void logout(String token) {
        JwtBlockList jwtBlockList = new JwtBlockList(token);
        jwtBlockListRepo.save(jwtBlockList);
    }

    public Object getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != ANONYMOUS_USER) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            return userDetails.getUser();
        } else {
            return null;
        }
    }
}

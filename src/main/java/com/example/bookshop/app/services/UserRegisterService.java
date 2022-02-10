package com.example.bookshop.app.services;

import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.app.config.security.UserDetails;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.app.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;

@Service
public class UserRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserRegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transient
    public void registerNewUser(RegistrationFormDto registrationFormDto) {

        if (userRepository.findUserByEmail(registrationFormDto.getEmail()) == null) {
            User user = new User();
            user.setName(registrationFormDto.getName());
            user.setEmail(registrationFormDto.getEmail());
            user.setPhone(registrationFormDto.getPhone());
            user.setPassword(passwordEncoder.encode(registrationFormDto.getPassword()));
            userRepository.save(user);
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                payload.getContact(), payload.getCode()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(true);
        return response;
    }

    public Object getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            UserDetails userDetails = (UserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            return userDetails.getUser();
        } else return null;

    }
}

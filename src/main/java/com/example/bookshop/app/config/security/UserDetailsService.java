package com.example.bookshop.app.config.security;

import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    private static final String EMAIL_PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String contact) throws UserNotFoundException {

        boolean isEmail = isEmail(contact);

        User user = isEmail ? userRepository.findUserByEmail(contact) : userRepository.findUserByPhone(contact);

        if (user != null) {
            return new UserDetails(user);
        } else {
            throw new UserNotFoundException(contact, isEmail);
        }
    }

    private boolean isEmail(String payload) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(payload);
        return matcher.matches();
    }
}

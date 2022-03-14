package com.example.bookshop.app.config.security;

import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    private static final String EMAIL_PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String contact) throws UsernameNotFoundException {

        User user = isEmail(contact)
                ? userRepository.findUserByEmail(contact)
                : userRepository.findUserByPhone(contact);

        if (user != null) {
            return new UserDetails(user);
        } else {
            throw new UsernameNotFoundException("user not found doh!");
        }
    }

    private boolean isEmail(String payload) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(payload);
        return matcher.matches();
    }
}

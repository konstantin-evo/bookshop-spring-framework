package com.example.bookshop.app.config.security;

import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.example.bookshop.app.services.RegexUtil.isEmail;

@Service
@RequiredArgsConstructor
public class BookshopUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String contact) throws UserNotFoundException {

        User user = isEmail(contact)
                ? userRepository.findUserByEmail(contact).orElseThrow(() -> new UserNotFoundException(contact, true))
                : userRepository.findUserByPhone(contact).orElseThrow(() -> new UserNotFoundException(contact, false));

        return new BookshopUserDetails(user);
    }

}

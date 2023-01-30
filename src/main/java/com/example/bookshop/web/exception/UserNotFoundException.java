package com.example.bookshop.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException(Integer id) {
        super("User not found. User ID: " + id);
    }

    public UserNotFoundException(String contact, boolean isEmail) {
        super("User not found. Contact is email: " + isEmail + ". Contact: " + contact);
    }

    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

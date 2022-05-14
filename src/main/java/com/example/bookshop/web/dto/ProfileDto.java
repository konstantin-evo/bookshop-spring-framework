package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Dto containing information about the user profile
 * (used to change data in the Profile: password, email, etc.)
 */
@Getter
@Setter
public class ProfileDto {
    private String name;
    private String mail;
    private String phone;
    private String password;
    private String passwordReply;
}

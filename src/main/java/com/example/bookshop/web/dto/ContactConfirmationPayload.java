package com.example.bookshop.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto object that is used to send user authentication data:
 *
 * contact - can be email or phone depending on the user's choice
 * code- one-time code that comes to the mail or phone
 */
@Getter
@Setter
@AllArgsConstructor
public class ContactConfirmationPayload {

    private String contact;
    private String code;

}

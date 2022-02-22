package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Dto object that is used to send user authentication data:
 * <p>
 * <b>contact</b> - can be email or phone depending on the user's choice <br>
 * <b>code</b> - one-time code that comes to the mail or phone
 * <p>
 * TODO: at the moment the one-time code functionality is not implemented
 *  and a user's password is used
 */
@Getter
@Setter
public class ContactConfirmationPayload {

    private String contact;
    private String code;

}

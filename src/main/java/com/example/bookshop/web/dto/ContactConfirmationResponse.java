package com.example.bookshop.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactConfirmationResponse {

    private String result;

    public ContactConfirmationResponse(boolean result) {
        this.result = result ? "true" : "false";
    }

}

package com.example.bookshop.app.model.entity.enumuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    USER("USER"),
    ADMIN("ADMIN");

    private final String value;
}

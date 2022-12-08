package com.example.bookshop.web.exception;

import lombok.Getter;

import javax.persistence.EntityNotFoundException;

@Getter
public class BookshopEntityNotFoundException extends EntityNotFoundException {

    private String entityName;
    private String message;

    public BookshopEntityNotFoundException(String message) {
        super(message);
    }

    public BookshopEntityNotFoundException(String message, String entityName, String parameter, String value) {
        super(message);
        this.entityName = entityName;
        this.message = "The " + entityName + " not found by parameter \"" + parameter + "\" with value \""
                + value + "\" .";
    }
}

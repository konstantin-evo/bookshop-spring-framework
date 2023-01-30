package com.example.bookshop.web.exception;

import lombok.Getter;

import javax.persistence.EntityNotFoundException;

@Getter
public class BookshopEntityNotFoundException extends EntityNotFoundException {

    private final String entityName;
    private final String message;

    public BookshopEntityNotFoundException(String entityName, String parameter, String value) {
        super("The " + entityName + " not found by parameter \"" + parameter + "\" with value \""
                + value + "\" .");
        this.entityName = entityName;
        this.message = "The " + entityName + " not found by parameter \"" + parameter + "\" with value \""
                + value + "\" .";
    }

    public BookshopEntityNotFoundException(String entityName, Integer id) {
        this(entityName, "Id", id.toString());
    }

}

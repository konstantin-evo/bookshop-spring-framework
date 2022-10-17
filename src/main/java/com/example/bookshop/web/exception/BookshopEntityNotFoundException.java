package com.example.bookshop.web.exception;

import javax.persistence.EntityNotFoundException;

public class BookshopEntityNotFoundException extends EntityNotFoundException {

    public BookshopEntityNotFoundException(Integer id, String entityName) {
        super("The " + entityName + " not found. id: " + id);
    }

    public BookshopEntityNotFoundException(String value, String field, String entityName) {
        super("The " + entityName + " not found by filed: " + field + " with value: " + value);
    }
}

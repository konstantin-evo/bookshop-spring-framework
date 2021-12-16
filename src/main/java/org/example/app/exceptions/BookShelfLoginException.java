package org.example.app.exceptions;

import lombok.Getter;

@Getter
public class BookShelfLoginException extends Exception {

    private final String message;

    public BookShelfLoginException(String message) {
        this.message = message;
    }

}

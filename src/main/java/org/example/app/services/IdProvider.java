package org.example.app.services;

import org.example.web.dto.Book;

import org.apache.log4j.Logger;

public class IdProvider {

    Logger logger = Logger.getLogger(IdProvider.class);

    public String provideId(Book book) {
        return this.hashCode() + "_" + book.hashCode();
    }
}

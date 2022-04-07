package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO object for interacting with GoogleBook API
 */
@Getter
@Setter
@NoArgsConstructor
public class BooksPageGoogleDto {

    private Integer count;
    private List<BookGoogleDto> books;

    public BooksPageGoogleDto(List<BookGoogleDto> books) {
        this.count = books.size();
        this.books = books;
    }
}

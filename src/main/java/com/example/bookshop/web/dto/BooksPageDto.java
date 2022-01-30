package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BooksPageDto {

    private Integer count;
    private List<BookDto> books;

    public BooksPageDto(List<BookDto> books) {
        this.count = books.size();
        this.books = books;
    }
}

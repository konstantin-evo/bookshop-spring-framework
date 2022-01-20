package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendedBooksPageDto {

    private Integer count;
    private List<BookDto> books;

    public RecommendedBooksPageDto(List<BookDto> books) {
        this.count = books.size();
        this.books = books;
    }

}

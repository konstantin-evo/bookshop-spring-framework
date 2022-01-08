package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookDto {

    private Integer id;
    private AuthorDto author;
    private String title;
    private String priceOld;
    private String price;
}

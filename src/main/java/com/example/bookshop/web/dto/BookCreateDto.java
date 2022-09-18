package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookCreateDto {
    private String title;
    private String description;
    private String author;
    private String genre;
    private Date pubDate;
    private int price;
    private int discount;
    private int isBestseller;
    private String tags;
}

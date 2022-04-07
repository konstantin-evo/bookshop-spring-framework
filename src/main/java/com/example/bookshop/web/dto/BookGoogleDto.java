package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO object for interacting with GoogleBook API
 */
@Getter
@Setter
public class BookGoogleDto {
    private String id;
    private String title;
    private String description;
    private String price;
    private String image;
    private String author;
    private String link;
}

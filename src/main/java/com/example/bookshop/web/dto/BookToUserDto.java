package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookToUserDto {
    private Integer bookToUserId;
    private String username;
    private String bookTitle;
    private String bookSlug;
    private String bookStatus;
}

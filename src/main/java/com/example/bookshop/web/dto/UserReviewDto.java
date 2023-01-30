package com.example.bookshop.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Using for display User's reviews on Admin Page
// to moderate it further
@Getter
@Setter
@Builder
public class UserReviewDto {
    private Integer id;
    private String text;
    private String pubDate;
    private String userName;
    private String bookTitle;
    private String bookSlug;
}

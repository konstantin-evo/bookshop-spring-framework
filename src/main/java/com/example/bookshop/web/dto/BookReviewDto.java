package com.example.bookshop.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Using for saving new BookReview by User on Book Page
// Information on Rating, Publication Date and so on is saved in advance using another Dto
@Getter
@Setter
@AllArgsConstructor
public class BookReviewDto {
    String text;
    String slug;
}

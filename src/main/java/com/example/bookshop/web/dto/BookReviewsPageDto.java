package com.example.bookshop.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Using for display information about Reviews on Book page
@Getter
@Setter
@Builder
public class BookReviewsPageDto {
    private Integer totalReviews;
    private List<Review> reviews;

    @Builder
    @Getter
    public static class  Review {
        private Integer id;
        private String text;
        private String pubDate;
        private String userName;
        private Integer rate;
        private Integer likes;
        private Integer dislikes;
    }
}

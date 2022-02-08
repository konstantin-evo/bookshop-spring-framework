package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookReviewRateRepository;
import com.example.bookshop.app.model.dao.BookReviewRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.BookReview;
import com.example.bookshop.app.model.entity.BookReviewRate;
import com.example.bookshop.app.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookReviewService {

    private final BookReviewRateRepository reviewRateRepo;
    private final BookReviewRepository reviewRepo;
    private final UserRepository userRepo;

    @Autowired
    public BookReviewService(BookReviewRateRepository reviewRateRepo,
                             BookReviewRepository reviewRepo,
                             UserRepository userRepo) {
        this.reviewRateRepo = reviewRateRepo;
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
    }

    /**
     * The method updates the "Like", "Dislike" counter for a specific review
     * If the User has previously rated the Review - the value is updated
     * If the User has not previously rated the Review - a new "Like-Dislike" is created
     *
     * @param value defines "Like" (1), or "Dislike" (-1)
     * @param userId unique user ID
     * @param reviewId unique review ID
     * @return true if the rating update was successful
     */
    public boolean setRateBookReview(Integer reviewId, Integer value, Integer userId) {

        if (reviewRateRepo.findByBookReviewAndUser(reviewId, userId) != null) {
            BookReviewRate bookReview = reviewRateRepo.findByBookReviewAndUser(reviewId, userId);
            bookReview.setRate(value);
            reviewRateRepo.save(bookReview);
        } else {
            BookReview bookReview = reviewRepo.findById(reviewId).orElse(null);
            User user = userRepo.findById(userId).orElse(null);
            reviewRateRepo.save(
                    BookReviewRate.builder()
                            .review(bookReview)
                            .user(user)
                            .rate(value)
                            .build());
        }
        return true;
    }
}

package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookRateService;
import com.example.bookshop.app.services.BookReviewService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.BookReviewDto;
import com.example.bookshop.web.dto.RateBookDto;
import com.example.bookshop.web.dto.RateReviewDto;
import com.example.bookshop.web.exception.BookRateNotFoundException;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class BookRateApiController {

    private final BookReviewService reviewService;
    private final BookRateService rateService;
    private final UserRegisterService userRegisterService;

    public BookRateApiController(BookReviewService reviewService,
                                 BookRateService rateService,
                                 UserRegisterService userRegisterService) {
        this.reviewService = reviewService;
        this.rateService = rateService;
        this.userRegisterService = userRegisterService;
    }

    @PostMapping("/rate-book")
    @ResponseBody
    public boolean handleRatingBook(@RequestBody RateBookDto bookRate) throws CustomAuthenticationException {
        User user = (User) userRegisterService.getCurrentUser();
        if (user != null) {
            return rateService.setBookRate(
                    bookRate.getBookId(),
                    bookRate.getValue(),
                    user.getId());
        } else {
            throw new CustomAuthenticationException("Please authenticate in order to rate the book.");
        }
    }

    @PostMapping("/rate-book-review")
    @ResponseBody
    public boolean handleRateBookReview(@RequestBody RateReviewDto rateReview) throws CustomAuthenticationException {
        User user = (User) userRegisterService.getCurrentUser();
        if (user != null) {
            return reviewService.setRateBookReview(
                    rateReview.getReviewId(),
                    rateReview.getValue(),
                    user.getId());
        } else {
            throw new CustomAuthenticationException("Please authenticate in order to rate the review.");
        }
    }

    @PostMapping("/book-review")
    @ResponseBody
    public boolean handleBookReview(@RequestBody BookReviewDto bookReview)
            throws CustomAuthenticationException, BookRateNotFoundException {
        User user = (User) userRegisterService.getCurrentUser();
        if (user != null) {
            return reviewService.saveBookReview(
                    bookReview.getBookId(),
                    bookReview.getText(),
                    user.getId());
        } else {
            throw new CustomAuthenticationException("Please authenticate in order to send the review.");
        }
    }

}

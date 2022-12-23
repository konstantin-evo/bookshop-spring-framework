package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookRateService;
import com.example.bookshop.app.services.BookReviewService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.RateBookDto;
import com.example.bookshop.web.dto.RateReviewDto;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookRateApiController {

    private final BookReviewService reviewService;
    private final BookRateService rateService;
    private final UserRegisterService userRegisterService;

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

}

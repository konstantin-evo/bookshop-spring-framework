package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.AdminService;
import com.example.bookshop.app.services.BookReviewService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.BookReviewDto;
import com.example.bookshop.web.dto.UserReviewDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookReviewApiController {

    private final BookReviewService bookReviewService;
    private final AdminService adminService;
    private final UserRegisterService userRegisterService;

    @GetMapping("/reviews")
    @ResponseBody
    public List<UserReviewDto> getBookReviews(@RequestParam("userId") Integer userId) {
        return bookReviewService.getReviewsByUser(userId);
    }

    @PostMapping("/reviews")
    @ResponseBody
    public boolean createBookReview(@RequestBody BookReviewDto bookReview)
            throws CustomAuthenticationException {
        User user = (User) userRegisterService.getCurrentUser();
        if (user != null) {
            return bookReviewService.saveBookReview(bookReview.getSlug(), bookReview.getText(), user);
        } else {
            throw new CustomAuthenticationException("Please authenticate in order to send the review.");
        }
    }

    @DeleteMapping("/reviews/{id}")
    @ResponseBody
    public ValidatedResponseDto deleteBookReview(@PathVariable Integer id) {
        return adminService.deleteReview(id);
    }

}

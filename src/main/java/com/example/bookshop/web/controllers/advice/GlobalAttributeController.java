package com.example.bookshop.web.controllers.advice;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.BookToUserService;
import com.example.bookshop.app.services.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
@RequiredArgsConstructor
public class GlobalAttributeController {

    private final BookService bookService;
    private final BookToUserService bookToUserService;
    private final UserRegisterService userRegisterService;

    @ModelAttribute("searchWord")
    public String searchWord() {
        return "";
    }

    @ModelAttribute(name = "bookCartCount")
    public Integer bookCartCount(@CookieValue(value = "cartContents", required = false) String cartContents) {
        return bookService.getBooksByCookies(cartContents).size();
    }

    @ModelAttribute("bookPaidCount")
    public Integer bookPaidCount() {
        User user = (User) userRegisterService.getCurrentUser();
        return (userRegisterService.getCurrentUser() != null)
                ? bookToUserService.getPaidBooks(user.getEmail()).size() : 0;
    }

    @ModelAttribute(name = "bookPostponedCount")
    public Integer bookPostponedCount(@CookieValue(value = "postponedBooks", required = false) String postponedBooks) {
        return bookService.getBooksByCookies(postponedBooks).size();
    }

}

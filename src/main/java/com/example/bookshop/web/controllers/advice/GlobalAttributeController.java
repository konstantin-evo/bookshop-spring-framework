package com.example.bookshop.web.controllers.advice;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;


@ControllerAdvice
@SessionAttributes("testBook")
@RequiredArgsConstructor
public class GlobalAttributeController {

    private final BookService bookService;
    private final UserRegisterService userRegisterService;

    @ModelAttribute("searchWord")
    public String searchWord() {
        return "";
    }

    @ModelAttribute(name = "bookCartCount")
    public Integer bookCartCount(@CookieValue(value = "cartContents", required = false) String cartContents) {
        return bookService.getBooksByCookies(cartContents).size();
    }

    @ModelAttribute("PaidBookCount")
    public Integer home() {
        if (userRegisterService.getCurrentUser() != null) {
            User user = (User) userRegisterService.getCurrentUser();
            return bookService.getPaidBooks(user.getEmail()).size();
        } else {
            return 0;
        }
    }

    @ModelAttribute(name = "bookPostponedCount")
    public Integer bookPostponedCount(@CookieValue(value = "postponedBooks", required = false) String postponedBooks) {
        return bookService.getBooksByCookies(postponedBooks).size();
    }

}

package com.example.bookshop.web.controllers.advice;

import com.example.bookshop.app.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class GlobalAttributeController {

    private final BookService bookService;

    @Autowired
    public GlobalAttributeController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchWord")
    public String searchWord() {
        return "";
    }

    @ModelAttribute(name = "bookCartCount")
    public Integer bookCartCount(@CookieValue(value = "cartContents", required = false) String cartContents) {
            return bookService.getBooksByCookies(cartContents).size();
    }

    @ModelAttribute(name = "bookPostponedCount")
    public Integer bookPostponedCount(@CookieValue(value = "postponedBooks", required = false) String postponedBooks) {
            return bookService.getBooksByCookies(postponedBooks).size();
    }

}

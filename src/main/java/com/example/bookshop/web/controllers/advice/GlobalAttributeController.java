package com.example.bookshop.web.controllers.advice;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

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

    @ModelAttribute(name = "bookCart")
    public List<BookDto> bookCart(@CookieValue(value = "cartContents", required = false) String cartContents) {
        if (cartContents == null || cartContents.equals("")) {
            return new ArrayList<>();
        } else {
            return bookService.getBooksInCart(cartContents);
        }
    }
}

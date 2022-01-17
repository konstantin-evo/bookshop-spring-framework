package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookPagesController {

    private final BookService bookService;

    @Autowired
    public BookPagesController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("booksList")
    public List<BookDto> bookList(){
        return bookService.getBooksData();
    }

    @GetMapping("/recent")
    public String recentPage(){
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularPage(){
        return "books/popular";
    }

}
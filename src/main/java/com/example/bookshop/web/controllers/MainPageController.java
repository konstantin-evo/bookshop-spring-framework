package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.RecommendedBooksPageDto;
import com.example.bookshop.web.dto.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookDto> recommendedBooks(){
        return bookService.getPageOfRecommendedBooks(0,6).getContent();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/")
    public String mainPage(){
        return "index";
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public RecommendedBooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                                @RequestParam("limit") Integer limit) {
        return new RecommendedBooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }
}
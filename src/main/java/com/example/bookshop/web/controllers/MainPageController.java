package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.TagService;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.List;


@Controller
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;

    private static final int OFFSET = 0;
    private static final int LIMIT = 20;
    private static final int DEFAULT_RECENT_MONTH = 6;

    @Autowired
    public MainPageController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookDto> recommendedBooks() {
        return bookService
                .getPageOfRecommendedBooks(OFFSET, LIMIT)
                .getContent();
    }

    @ModelAttribute("recentBooks")
    public List<BookDto> recentBooks() {
        LocalDate dateTo = LocalDate.now();
        LocalDate dateFrom = dateTo.minusMonths(DEFAULT_RECENT_MONTH);
        return bookService
                .getPageOfRecentBooks(dateFrom, dateTo, OFFSET, LIMIT)
                .getContent();
    }

    @ModelAttribute("popularBooks")
    public List<BookDto> popularBooks() {
        return bookService
                .getPageOfPopularBooks(OFFSET,LIMIT)
                .getContent();
    }

    @ModelAttribute("from")
    public LocalDate dateFrom() {
        return LocalDate.now().minusMonths(DEFAULT_RECENT_MONTH);
    }

    @ModelAttribute("to")
    public LocalDate dateTo() {
        return LocalDate.now();
    }

    @ModelAttribute("tags")
    public List<TagDto> tags(){
        return tagService.getTagsData();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

}
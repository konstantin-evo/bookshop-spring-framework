package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookPagesController {

    private final BookService bookService;
    private static final int OFFSET = 0;
    private static final int LIMIT = 6;
    private static final int DEFAULT_RECENT_MONTH = 6;

    @Autowired
    public BookPagesController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("booksList")
    public List<BookDto> bookList(){
        return bookService.getBooksData();
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
        LocalDate dateFrom = LocalDate.now().minusMonths(DEFAULT_RECENT_MONTH);
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

    @GetMapping("/popular")
    public String popularPage(){
        return "books/popular";
    }


    @GetMapping("/recent")
    public String recentPage(){
        return "books/recent";
    }

}

package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.TagService;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;

    @Value("${default.offset}")
    private int OFFSET;
    @Value("${default.limit}")
    private int LIMIT;
    @Value("${default.recentmonth}")
    private int DEFAULT_RECENT_MONTH;

    private final LocalDate dateTo = LocalDate.of(2021, 12, 31);
    private final LocalDate dateFrom = dateTo.minusMonths(DEFAULT_RECENT_MONTH);

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @ModelAttribute("recommendedBooks")
    public List<BookDto> recommendedBooks() {
        return bookService
                .getPageOfRecommendedBooks(OFFSET, LIMIT)
                .getContent();
    }

    @ModelAttribute("recentBooks")
    public List<BookDto> recentBooks() {
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
        return dateFrom;
    }

    @ModelAttribute("to")
    public LocalDate dateTo() {
        return dateTo;
    }

    @ModelAttribute("tags")
    public List<TagDto> tags(){
        return tagService.getTagsData();
    }

}
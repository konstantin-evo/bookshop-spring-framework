package com.example.bookshop.web.controllers.book;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookRecentController {

    private final BookService bookService;

    @Value("${default.offset}")
    private int OFFSET;
    @Value("${default.limit}")
    private int LIMIT;
    @Value("${default.recentmonth}")
    private int DEFAULT_RECENT_MONTH;

    @GetMapping("/recent")
    public String recentPage(Model model) {
        model.addAttribute("recentBooks", recentBooks());
        return "books/recent";
    }

    private List<BookDto> recentBooks() {
        LocalDate dateTo = LocalDate.of(2021, 12, 31);
        LocalDate dateFrom = dateTo.minusMonths(DEFAULT_RECENT_MONTH);
        return bookService
                .getPageOfRecentBooks(dateFrom, dateTo, OFFSET, LIMIT)
                .getContent();
    }
}

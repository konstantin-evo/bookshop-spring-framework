package com.example.bookshop.web.controllers.book;

import com.example.bookshop.app.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookPopularController {

    private final BookService bookService;

    @Value("${default.offset}")
    private int offset;
    @Value("${default.limit}")
    private int limit;

    @GetMapping("/popular")
    public String popularPage(Model model) {
        model.addAttribute("popularBooks", bookService
                .getPageOfPopularBooks(offset, limit)
                .getContent());
        return "books/popular";
    }

}

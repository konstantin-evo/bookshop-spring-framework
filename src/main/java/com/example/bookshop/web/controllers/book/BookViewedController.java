package com.example.bookshop.web.controllers.book;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookViewedController {

    private final BookService bookService;
    private final UserRegisterService userRegisterService;

    @Value("${default.offset}")
    private int OFFSET;
    @Value("${default.limit}")
    private int LIMIT;

    @GetMapping("/viewed")
    public String popularPage(Model model) {
        User user = (User) userRegisterService.getCurrentUser();
        model.addAttribute("viewedBooks", bookService
                .getPageOfViewedBooks(user, OFFSET, LIMIT)
                .getContent());
        return "books/viewed";
    }

}

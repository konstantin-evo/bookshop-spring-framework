package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/books")
public class PostponedPageController {

    private final BookService bookService;

    @Value("${default.offset}")
    private int OFFSET;
    @Value("${default.limit}")
    private int LIMIT;

    @Autowired
    public PostponedPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute(name = "postponedBooks")
    public List<BookDto> bookPostponed(@CookieValue(value = "postponedBooks", required = false) String bookPostponed) {
        if (bookPostponed == null || bookPostponed.equals("")) {
            return new ArrayList<>();
        } else {
            return bookService.getBooksByCookies(bookPostponed);
        }
    }

    @GetMapping("/postponed")
    public String postponedPage(@CookieValue(value = "postponedBooks", required = false) String postponedBooks,
                                Model model) {
        if (postponedBooks == null || postponedBooks.equals("")) {
            model.addAttribute("recommendedBooks", bookService
                    .getPageOfRecommendedBooks(OFFSET, LIMIT)
                    .getContent());
        }
        model.addAttribute("isPostponedEmpty", bookService.getBooleanAttribute(postponedBooks));
        return "postponed";
    }

    @PostMapping("/changeBookStatus/postpone/remove/{slug}")
    public String removeBookFromPostponed(@PathVariable("slug") String slug,
                                          @CookieValue(name = "postponedBooks", required = false) String postponedBooks,
                                          HttpServletResponse response, Model model) {

        response.addCookie(bookService.removeBookFromCookie(postponedBooks, "postponedBooks", slug));
        model.addAttribute("isPostponedEmpty", bookService.getBooleanAttribute(postponedBooks));

        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/kept/{slug}")
    public String handleChangeBookStat(@PathVariable("slug") String slug,
                                       @CookieValue(name = "postponedBooks", required = false) String postponedBooks,
                                       HttpServletResponse response, Model model) {

        response.addCookie(bookService.getUpdatedCookies(postponedBooks, "postponedBooks", slug));
        model.addAttribute("isPostponedEmpty", bookService.getBooleanAttribute(postponedBooks));

        return "redirect:/books/" + slug;
    }

}

package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.services.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class PostponedPageController {

    private final BookService bookService;

    @Value("${default.offset}")
    private int offset;
    @Value("${default.limit}")
    private int limit;

    @ModelAttribute(name = "postponedBooks")
    public List<BookDto> bookPostponed(@CookieValue(value = "postponedBooks", required = false) String bookPostponed) {
        return CookieUtil.isCookieEmpty(bookPostponed)
                ? new ArrayList<>()
                : bookService.getBooksByCookies(bookPostponed);
    }

    @ModelAttribute(name = "isPostponedEmpty")
    public boolean isPostponedEmpty(@CookieValue(value = "postponedBooks", required = false) String postponedBooks) {
        return CookieUtil.isCookieEmpty(postponedBooks);
    }

    @GetMapping("/postponed")
    public String postponedPage(@CookieValue(value = "postponedBooks", required = false) String postponedBooks,
                                Model model) {
        if (CookieUtil.isCookieEmpty(postponedBooks)) {
            model.addAttribute("recommendedBooks", bookService
                    .getPageOfRecommendedBooks(offset, limit)
                    .getContent());
        }
        return "postponed";
    }

    @PostMapping("/changeBookStatus/postpone/remove/{slug}")
    public String removeBookFromPostponed(@PathVariable("slug") String slug,
                                          @CookieValue(name = "postponedBooks", required = false) String postponedBooks,
                                          HttpServletResponse response) {
        CookieUtil.removeBookFromCookie(postponedBooks, "postponedBooks", slug, response);
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/kept/{slug}")
    public String handleChangeBookStat(@PathVariable("slug") String slug,
                                       @CookieValue(name = "postponedBooks", required = false) String postponedBooks,
                                       HttpServletResponse response) {
        CookieUtil.updateCookie(postponedBooks, "postponedBooks", slug, response);
        return "redirect:/books/" + slug;
    }

}

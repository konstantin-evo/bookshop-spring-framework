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
public class CartPageController {

    private final BookService bookService;

    @Value("${default.offset}")
    private int OFFSET;
    @Value("${default.limit}")
    private int LIMIT;

    @Autowired
    public CartPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute(name = "bookCart")
    public List<BookDto> bookCart(@CookieValue(value = "cartContents", required = false) String cartContents) {
        if (cartContents == null || cartContents.equals("")) {
            return new ArrayList<>();
        } else {
            return bookService.getBooksByCookies(cartContents);
        }
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("popularBooks", bookService
                    .getPageOfPopularBooks(OFFSET, LIMIT)
                    .getContent());
        } else {
            model.addAttribute("totalPrices", bookService.getTotalPricesInCart(cartContents));
        }
        model.addAttribute("isCartEmpty", bookService.getBooleanAttribute(cartContents));
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCart(@PathVariable("slug") String slug,
                                           @CookieValue(name = "cartContents", required = false) String cartContents,
                                           HttpServletResponse response, Model model) {

        response.addCookie(bookService.removeBookFromCookie(cartContents, "cartContents", slug));
        model.addAttribute("isCartEmpty", bookService.getBooleanAttribute(cartContents));

        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/buy/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         HttpServletResponse response, Model model) {

        response.addCookie(bookService.getUpdatedCookies(cartContents, "cartContents", slug));
        model.addAttribute("isCartEmpty", bookService.getBooleanAttribute(cartContents));

        return "redirect:/books/" + slug;
    }

}

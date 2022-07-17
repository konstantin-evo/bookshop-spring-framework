package com.example.bookshop.web.controllers;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.BalanceResponseDto;
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
public class CartPageController {

    private final BookService bookService;
    private final UserRegisterService userRegisterService;

    @Value("${default.offset}")
    private int OFFSET;
    @Value("${default.limit}")
    private int LIMIT;

    @ModelAttribute(name = "bookCart")
    public List<BookDto> bookCart(@CookieValue(value = "cartContents", required = false) String cartContents) {
        return CookieUtil.isCookieEmpty(cartContents)
                ? new ArrayList<>()
                : bookService.getBooksByCookies(cartContents);
    }

    @ModelAttribute(name = "isCartEmpty")
    public boolean isCartEmpty(@CookieValue(value = "cartContents", required = false) String cartContents) {
        return CookieUtil.isCookieEmpty(cartContents);
    }

    @ModelAttribute(name = "userBalance")
    public int userBalance() {
        User user = (User) userRegisterService.getCurrentUser();
        return (user != null) ? user.getBalance() : 0;
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        if (CookieUtil.isCookieEmpty(cartContents)) {
            model.addAttribute("popularBooks", bookService
                    .getPageOfPopularBooks(OFFSET, LIMIT)
                    .getContent());
        } else {
            model.addAttribute("totalPrices", bookService.getTotalPricesInCart(cartContents));
        }
        return "cart";
    }

    @PostMapping("/order")
    @ResponseBody
    public BalanceResponseDto handleOrderBook(@CookieValue(name = "cartContents", required = false) String cartContents,
                                              HttpServletResponse response) {
        User user = (User) userRegisterService.getCurrentUser();
        boolean booksBought = bookService.orderBooks(cartContents, user, response);
        return new BalanceResponseDto(booksBought);
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCart(@PathVariable("slug") String slug,
                                           @CookieValue(name = "cartContents", required = false) String cartContents,
                                           HttpServletResponse response) {
        CookieUtil.removeBookFromCookie(cartContents, "cartContents", slug, response);
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/buy/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         HttpServletResponse response) {
        CookieUtil.updateCookie(cartContents, "cartContents", slug, response);
        return "redirect:/books/" + slug;
    }

}

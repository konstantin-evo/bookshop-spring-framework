package com.example.bookshop.web.controllers;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.BookToUserService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import com.example.bookshop.web.services.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class CartPageController {

    private final BookService bookService;
    private final BookToUserService bookToUserService;
    private final UserRegisterService userRegisterService;

    @Value("${default.offset}")
    private int offset;
    @Value("${default.limit}")
    private int limit;

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
                    .getPageOfPopularBooks(offset, limit)
                    .getContent());
        } else {
            model.addAttribute("totalPrices", bookService.getTotalPricesInCart(cartContents));
        }
        return "cart";
    }

    @PostMapping("/order")
    @ResponseBody
    public ValidatedResponseDto handleOrderBook(@CookieValue(name = "cartContents", required = false) String cartContents,
                                                HttpServletResponse response) {
        User user = (User) userRegisterService.getCurrentUser();
        return bookToUserService.orderBooks(cartContents, user, response);
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

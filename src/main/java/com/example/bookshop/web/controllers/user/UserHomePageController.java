package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookToUserService;
import com.example.bookshop.app.services.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserHomePageController {

    private final BookToUserService bookToUserService;
    private final UserRegisterService userRegisterService;

    @GetMapping("/home")
    public String userHomePage(Model model) {
        User user = (User) userRegisterService.getCurrentUser();
        model.addAttribute("paidBooks", bookToUserService.getPaidBooks(user.getEmail()));
        return "home";
    }
}

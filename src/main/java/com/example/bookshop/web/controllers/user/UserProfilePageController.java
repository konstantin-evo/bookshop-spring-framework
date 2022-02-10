package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.services.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfilePageController {

    private final UserRegisterService userRegisterService;

    @Autowired
    public UserProfilePageController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("currentUser", userRegisterService.getCurrentUser());
        return "profile";
    }
}

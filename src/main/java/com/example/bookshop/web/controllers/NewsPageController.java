package com.example.bookshop.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewsPageController {

    @GetMapping("/news")
    public String mainPage(Model model){
        return "books/recent";
    }
}

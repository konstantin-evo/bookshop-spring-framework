package com.example.bookshop.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookshop")
public class GenresPageController {

    @GetMapping("/genres")
    public String mainPage(Model model){
        return "genres/index";
    }
}

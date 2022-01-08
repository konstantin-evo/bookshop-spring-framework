package com.example.bookshop.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class GenresPageController {

    @GetMapping("/genres")
    public String mainPage(Model model){
        return "genres/index";
    }
}

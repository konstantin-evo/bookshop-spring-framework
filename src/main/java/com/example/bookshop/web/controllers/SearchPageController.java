package com.example.bookshop.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchPageController {

    @GetMapping("/search")
    public String searchPage(){
        return "search/index";
    }

}

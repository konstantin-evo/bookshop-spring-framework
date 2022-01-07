package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookshop")
public class AuthorsPageController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsPageController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String mainPage(Model model){
        model.addAttribute("authorData", authorService.getAuthorsData());
        return "authors/index";
    }
}

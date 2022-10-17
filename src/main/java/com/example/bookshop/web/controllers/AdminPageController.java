package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.AdminService;
import com.example.bookshop.app.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {

    private final GenreService genreService;

    @ModelAttribute("genres")
    public List<String> getGenres() {
        return genreService.getGenresName();
    }

    @GetMapping("/book")
    public String adminBookPage(){
        return "admin/book";
    }

    @GetMapping("/author")
    public String adminAuthorPage(){
        return "admin/author";
    }

    @GetMapping("/review")
    public String adminReviewPage(){
        return "admin/review";
    }

    @GetMapping("/user")
    public String adminUserPage(){
        return "admin/user";
    }
}

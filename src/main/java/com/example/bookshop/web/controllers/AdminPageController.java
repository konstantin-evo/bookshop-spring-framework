package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.AdminService;
import com.example.bookshop.app.services.GenreService;
import com.example.bookshop.web.dto.BookCreateDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminService adminService;
    private final GenreService genreService;

    @ModelAttribute("genres")
    public List<String> getGenres() {
        return genreService.getGenresName();
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }

    @PostMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidatedResponseDto createBook(@RequestBody BookCreateDto bookDto) {
        return adminService.createBook(bookDto);
    }
}

package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GenresPageController {

    private final GenreService genreService;
    private final BookService bookService;

    @Autowired
    public GenresPageController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @GetMapping("/genres")
    public String genresPage(Model model){
        model.addAttribute("genres", genreService.getGenresData());
        return "genres/index";
    }

    @GetMapping("/genres/{id}")
    public String genreSlugPage(@PathVariable Integer id,
                                @RequestParam( value = "offset", defaultValue = "0") Integer offset,
                                @RequestParam(value = "limit", defaultValue = "6") Integer limit,
                                Model model){
        model.addAttribute("books", bookService
                .getPageOfBooksByGenre(offset, limit, id)
                .getContent());
        model.addAttribute("genre", genreService.getGenreData(id));
        return "genres/slug";
    }
}

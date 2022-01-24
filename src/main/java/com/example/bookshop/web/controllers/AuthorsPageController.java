package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.AuthorService;
import com.example.bookshop.app.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthorsPageController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsPageController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("/authors")
    public String authorsPage(Model model){
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        return "authors/index";
    }

    @GetMapping("/authors/{id}")
    public String authorSlugPage(@PathVariable Integer id,
                                @RequestParam( value = "offset", defaultValue = "0") Integer offset,
                                @RequestParam(value = "limit", defaultValue = "6") Integer limit,
                                Model model){
        model.addAttribute("books", bookService
                .getBooksByAuthorId(offset, limit, id)
                .getContent());
        model.addAttribute("author", authorService.getAuthor(id));
        return "authors/slug";
    }

    @GetMapping("/books/author/{id}")
    public String authorsBookPage(@PathVariable Integer id,
                                @RequestParam( value = "offset", defaultValue = "0") Integer offset,
                                @RequestParam(value = "limit", defaultValue = "6") Integer limit,
                                Model model){
        model.addAttribute("books", bookService
                .getBooksByAuthorId(offset, limit, id)
                .getContent());
        model.addAttribute("author", authorService.getAuthor(id));
        return "books/author";
    }
}

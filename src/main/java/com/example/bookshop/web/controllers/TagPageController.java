package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TagPageController {

    private final BookService bookService;
    private final TagService tagService;

    @Autowired
    public TagPageController(BookService bookService,TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }

    @GetMapping("/tags/{id}")
    public String tagPage(@PathVariable Integer id,
                          @RequestParam( value = "offset", defaultValue = "0") Integer offset,
                          @RequestParam(value = "limit", defaultValue = "6") Integer limit,
                          Model model){
        model.addAttribute("books", bookService
                .getPageOfBooksByTag(offset, limit, id)
                .getContent());
        model.addAttribute("tag", tagService.getTag(id));
        return "/tags/index";
    }
}

package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchPageController {

    private final BookService bookService;

    private static final int OFFSET = 0;
    private static final int LIMIT = 6;

    @Autowired
    public SearchPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchResults")
    public List<BookDto> searchResults() {
        return new ArrayList<>();
    }

    @GetMapping(value = "/search/{searchWord}")
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) {
        //TODO: Refactor index.html (upload file is not working with js)
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResults",
                bookService
                        .getPageOfSearchResultBooks(searchWordDto.getExample(), OFFSET, LIMIT)
                        .getContent());
        return "/search/index";
    }

    @GetMapping(value = {"/search"})
    public String getSearchPage() {
        return "/search/index";
    }

}

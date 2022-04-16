package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.GoogleApiService;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchPageController {

    private final GoogleApiService googleService;

    @Value("${default.offset}")
    private int OFFSET;
    @Value("${default.limit}")
    private int LIMIT;

    public SearchPageController(GoogleApiService googleService) {
        this.googleService = googleService;
    }

    @ModelAttribute("searchResults")
    public List<BookDto> searchResults() {
        return new ArrayList<>();
    }

    @GetMapping(value = "/search/{searchWord}")
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) String searchWord,
                                  Model model) {
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("searchResults", googleService
                .getPageOfSearchResult(searchWord, OFFSET, LIMIT));
        return "search/index";
    }

    @GetMapping(value = {"/search"})
    public String getSearchPage() {
        return "search/index";
    }

}

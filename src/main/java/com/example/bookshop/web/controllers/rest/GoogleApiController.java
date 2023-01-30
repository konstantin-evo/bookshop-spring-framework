package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.services.GoogleApiService;
import com.example.bookshop.web.dto.BooksPageGoogleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GoogleApiController {

    private final GoogleApiService googleService;

    @GetMapping("/api/search/{searchWord}")
    @ResponseBody
    public BooksPageGoogleDto getBooksBySearchQuery(@PathVariable String searchWord,
                                                    @RequestParam("offset") Integer offset,
                                                    @RequestParam("limit") Integer limit) {
        return new BooksPageGoogleDto(googleService
                .getPageOfSearchResult(searchWord, offset, limit));
    }
}

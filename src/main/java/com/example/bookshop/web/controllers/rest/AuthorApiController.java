package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.services.AdminService;
import com.example.bookshop.app.services.AuthorService;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(value = "author data api")
@RequiredArgsConstructor
public class AuthorApiController {

    private final AuthorService authorService;
    private final AdminService adminService;

    @GetMapping("/authors/{slug}")
    @ResponseBody
    public AuthorDto getAuthorBySlug(@PathVariable String slug) {
        return authorService.getAuthor(slug);
    }

    @PatchMapping(value = "/authors/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidatedResponseDto editAuthor(@RequestBody AuthorDto authorDto, @PathVariable String slug) {
        return adminService.editAuthor(authorDto, slug);
    }
}

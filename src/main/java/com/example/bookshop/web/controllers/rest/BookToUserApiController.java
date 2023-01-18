package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.services.AdminService;
import com.example.bookshop.app.services.BookToUserService;
import com.example.bookshop.web.dto.BookToUserDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/book-to-user")
@RequiredArgsConstructor
public class BookToUserApiController {

    private final BookToUserService bookToUserService;
    private final AdminService adminService;

    @GetMapping
    @ResponseBody
    public List<BookToUserDto> getBooksByUser(@RequestParam("userId") Integer userId) {
        return bookToUserService.getBooksByUser(userId);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidatedResponseDto createBookToUser(@RequestBody BookToUserDto bookToUserDto) {
        return adminService.createBookToUser(bookToUserDto);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidatedResponseDto editBookToUser(@RequestBody BookToUserDto bookToUserDto, @PathVariable Integer id) {
        return adminService.editBookToUser(bookToUserDto, id);
    }

}

package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.services.AdminService;
import com.example.bookshop.web.dto.UserDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final AdminService adminService;

    @PatchMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidatedResponseDto editUser(@RequestBody UserDto userDto, @PathVariable Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return adminService.editUser(userDto, userId, authentication);
    }
}

package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.ChangeProfileService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.ProfileDto;
import com.example.bookshop.web.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserProfilePageController {

    private final UserRegisterService userRegisterService;
    private final ChangeProfileService changeProfileService;

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("currentUser", userRegisterService.getCurrentUser());
        return "profile";
    }

    @PostMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProfileResponseDto handleChangeProfile(@RequestBody ProfileDto profileInfo) {
        User user = (User) userRegisterService.getCurrentUser();
        return changeProfileService.changeProfileInfo(profileInfo, user);
    }
}


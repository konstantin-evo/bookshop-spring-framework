package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.ChangeProfileService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.ProfileDto;
import com.example.bookshop.web.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserProfilePageController {

    private final UserRegisterService userRegisterService;
    private final ChangeProfileService changeProfileService;

    @ModelAttribute("currentUser")
    public User searchWord() {
        return (User) userRegisterService.getCurrentUser();
    }

    @GetMapping("/profile")
    public String handleProfile() {
        return "profile";
    }

    @PostMapping(value = "/profile")
    public String handleTopUpAccount(String sum) {
        User user = (User) userRegisterService.getCurrentUser();
        changeProfileService.topUpAccount(Integer.valueOf(sum), user);
        return "profile";
    }

    @PostMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProfileResponseDto handleChangeProfile(@RequestBody ProfileDto profileInfo) {
        User user = (User) userRegisterService.getCurrentUser();
        return changeProfileService.changeProfileInfo(profileInfo, user);
    }
}


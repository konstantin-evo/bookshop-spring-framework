package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.TransactionService;
import com.example.bookshop.app.services.UserProfileService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.ProfileDto;
import com.example.bookshop.web.dto.ProfileResponseDto;
import com.example.bookshop.web.dto.TransactionPageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserProfilePageController {

    private final UserRegisterService userRegisterService;
    private final UserProfileService changeProfileService;
    private final TransactionService transactionService;

    @Value("${default.offset}")
    private int OFFSET;
    @Value("${default.limit}")
    private int LIMIT;

    @ModelAttribute("currentUser")
    public User searchWord() {
        return (User) userRegisterService.getCurrentUser();
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        User user = (User) userRegisterService.getCurrentUser();
        model.addAttribute("transactions", transactionService
                .getPageOfTransaction(user, OFFSET, LIMIT)
                .getContent());
        return "profile";
    }

    @PostMapping(value = "/topUp")
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

    @GetMapping("/profile/confirmChanges")
    public String confirmRegistration(@RequestParam("token") String token) {
        changeProfileService.confirmProfileChanges(token);
        return "redirect:/profile";
    }

    /**
     * The controller is used to retrieve the user's Transaction History page.
     * Used to display the page correctly with JS
     */
    @GetMapping("/transactions")
    @ResponseBody
    public TransactionPageDto getTransactionPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        User user = (User) userRegisterService.getCurrentUser();
        return new TransactionPageDto(transactionService.getPageOfTransaction(user, offset, limit).getContent());
    }
}


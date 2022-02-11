package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.web.dto.RegistrationFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserAuthController {

    private final UserRegisterService userRegisterService;

    @Autowired
    public UserAuthController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @GetMapping("/signin")
    public String handleSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("registrationForm", new RegistrationFormDto());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(
            @RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/registration")
    public String handleUserRegistration(RegistrationFormDto registrationFormDto, Model model) {
        userRegisterService.registerNewUser(registrationFormDto);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userRegisterService.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }
}

package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.services.OneTimeCodeService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

@Controller
public class UserAuthController {

    private final UserRegisterService userRegisterService;
    private final OneTimeCodeService oneTimeCodeService;

    public UserAuthController(UserRegisterService userRegisterService,
                              OneTimeCodeService oneTimeCodeService) {
        this.userRegisterService = userRegisterService;
        this.oneTimeCodeService = oneTimeCodeService;
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
            @RequestBody ContactConfirmationPayload payload) throws NoSuchAlgorithmException {

        ContactConfirmationResponse response = new ContactConfirmationResponse(true);
        userRegisterService.contactConfirmation(payload.getContact());

        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        return oneTimeCodeService.verifyCode(payload.getCode())
                ? new ContactConfirmationResponse(true)
                : new ContactConfirmationResponse();
    }

    @PostMapping("/registration")
    public String handleUserRegistration(RegistrationFormDto registrationFormDto, Model model) {
        boolean registrationIsOk = userRegisterService.registerNewUser(registrationFormDto);
        model.addAttribute("regOk", registrationIsOk);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse)
            throws CustomAuthenticationException {
        ContactConfirmationResponse loginResponse = userRegisterService.login(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }
}

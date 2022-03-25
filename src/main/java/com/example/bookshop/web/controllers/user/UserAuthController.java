package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.config.security.SmsService;
import com.example.bookshop.app.model.entity.SmsCode;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.web.dto.RegistrationFormDto;
import org.springframework.beans.factory.annotation.Value;
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
    private final SmsService smsService;

    @Value("${twilio.expire_time_sec}")
    private int EXPIRE_TIME_SEC;

    public UserAuthController(UserRegisterService userRegisterService, SmsService smsService) {
        this.userRegisterService = userRegisterService;
        this.smsService = smsService;
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
            @RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse(true);

        if (!payload.getContact().contains("@")) {
            String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
            smsService.saveNewCode(new SmsCode(smsCodeString, EXPIRE_TIME_SEC));
        }

        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        return smsService.verifyCode(payload.getCode())
                ? new ContactConfirmationResponse(true)
                : new ContactConfirmationResponse();
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
        ContactConfirmationResponse loginResponse = userRegisterService.login(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }
}

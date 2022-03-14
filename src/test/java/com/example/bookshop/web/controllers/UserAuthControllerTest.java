package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class UserAuthControllerTest {

    private final MockMvc mockMvc;
    private final UserRegisterService userRegisterService;

    private static final String USER_REGISTER_PAYLOAD = "name=resourceOrderId&phone=%2B7+%28931%29+351-84-01&phoneCode=111+111&email=konstantin180692%40gmail.com&mailCode=000+000&password=111111";
    private static final String USER_EMAIL = "admin@gmail.com";
    private static final String USER_PHONE = "+7 (931) 000-00-01";
    private static final String USER_CODE = "111 111";
    private static final String COOKIE_JWT_NAME = "token";

    @Autowired
    public UserAuthControllerTest(MockMvc mockMvc,
                                  UserRegisterService userRegisterService) {
        this.mockMvc = mockMvc;
        this.userRegisterService = userRegisterService;
    }

    @Test
    public void userRegistrationTest() throws Exception {
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content(USER_REGISTER_PAYLOAD)
                )
                .andExpect(model().attribute("regOk", true))
                .andExpect(view().name("signin"))
                .andExpect(status().isOk());
    }

    @RetryingTest(maxAttempts = 3)
    public void userLoginEmailTest() throws Exception {

        ContactConfirmationPayload payload = generateContactConfirmationEmail();
        ContactConfirmationResponse loginResponse = userRegisterService.login(payload);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(payload))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(cookie().value("token", loginResponse.getResult()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").isString());
    }

    @RetryingTest(maxAttempts = 3)
    public void userLoginPhoneTest() throws Exception {

        ContactConfirmationPayload payload = generateContactConfirmationPhone();
        ContactConfirmationResponse loginResponse = userRegisterService.login(payload);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(payload))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(cookie().value(COOKIE_JWT_NAME, loginResponse.getResult()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").isString());
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    public void userLogoutTest() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(cookie().value(COOKIE_JWT_NAME, nullValue(String.class)))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ContactConfirmationPayload generateContactConfirmationEmail() {
        return new ContactConfirmationPayload(USER_EMAIL, USER_CODE);
    }

    private ContactConfirmationPayload generateContactConfirmationPhone() {
        return new ContactConfirmationPayload(USER_PHONE, USER_CODE);
    }

}
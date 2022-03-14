package com.example.bookshop.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class CartPageControllerTest {

    private final MockMvc mockMvc;

    private static final String BOOK_SLUG = "book-aca-pke";
    private static final String COOKIE_NAME = "cartContents";
    private static final Cookie CART_CONTENTS_COOKIE = new Cookie(COOKIE_NAME, BOOK_SLUG);

    @Autowired
    public CartPageControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void putBookInCartTest() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/buy/{slug}", BOOK_SLUG))
                .andExpect(cookie().value(COOKIE_NAME, BOOK_SLUG))
                .andExpect(redirectedUrl("/books/" + BOOK_SLUG))
                .andExpect(status().isFound());
    }

    @Test
    public void removeBookFromCartTest() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/cart/remove/{slug}", BOOK_SLUG)
                        .cookie(CART_CONTENTS_COOKIE))
                .andExpect(cookie().value(COOKIE_NAME, emptyOrNullString()))
                .andExpect(redirectedUrl("/books/cart"))
                .andExpect(status().isFound());
    }

}
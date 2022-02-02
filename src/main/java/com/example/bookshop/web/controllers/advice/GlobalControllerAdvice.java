package com.example.bookshop.web.controllers.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * The method is necessary in order to add the "searchWord" ModelAttribute to each controller
     * due to the fact that the search field is on each page (presence in header)
     */
    @ModelAttribute("searchWord")
    public String searchWord() {
        return "";
    }
}

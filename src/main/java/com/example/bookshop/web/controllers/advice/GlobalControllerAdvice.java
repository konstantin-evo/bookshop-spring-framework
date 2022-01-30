package com.example.bookshop.web.controllers.advice;

import com.example.bookshop.web.dto.SearchWordDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * The method is necessary in order to add the "searchWordDto" ModelAttribute to each controller
     * due to the fact that the search field is on each page (presence in header)
     */
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }
}

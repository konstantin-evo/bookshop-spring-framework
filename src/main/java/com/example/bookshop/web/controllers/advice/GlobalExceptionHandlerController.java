package com.example.bookshop.web.controllers.advice;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.web.dto.ApiResponse;
import com.example.bookshop.web.exception.BookstoreApiWrongParameterException;
import com.example.bookshop.web.exception.EmptySearchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing required parameters", exception),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler(BookstoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.BAD_REQUEST,
                "Bad parameter value...",
                exception),
                HttpStatus.BAD_REQUEST);
    }
}

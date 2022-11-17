package com.example.bookshop.web.controllers.advice;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookReview;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.ApiResponse;
import com.example.bookshop.web.exception.BookRateNotFoundException;
import com.example.bookshop.web.exception.BookstoreApiWrongParameterException;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import com.example.bookshop.web.exception.EmptySearchException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
@Log4j2
public class GlobalExceptionHandlerController {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception e) {
        log.error("MissingServletRequestParameterException occurred. {}", e.getLocalizedMessage());
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing required parameters", e),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        log.error("EmptySearchException has occurred. {}", e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler(BookstoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception e) {
        log.error("BookstoreApiWrongParameterException. {}", e.getLocalizedMessage());
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                e),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<ApiResponse<User>> handleCustomAuthenticationException(Exception e) {
        log.error("CustomAuthenticationException has occurred. {}", e.getLocalizedMessage());
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.UNAUTHORIZED,
                e.getMessage(),
                e),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BookRateNotFoundException.class)
    public ResponseEntity<ApiResponse<BookReview>> handleCustomBookRateNotFoundException(Exception e) {
        log.error("BookRateNotFoundException has occurred. {}", e.getLocalizedMessage());
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                e),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse<MultipartFile>> handleMultipartException(MultipartException e) {
        log.error("MultipartException has occurred. {}", e.getLocalizedMessage());
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                e),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse<MultipartFile>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        log.error("MissingServletRequestPartException has occurred. {}", e.getLocalizedMessage());
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                e),
                HttpStatus.BAD_REQUEST);
    }
}

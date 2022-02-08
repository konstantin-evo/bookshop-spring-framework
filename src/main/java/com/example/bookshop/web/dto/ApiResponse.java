package com.example.bookshop.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeStamp;
    private String message;
    private String debugMessage;
    private Collection<T> data;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(HttpStatus httpStatus, String message, Throwable ex) {
        this();
        this.status = httpStatus;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}

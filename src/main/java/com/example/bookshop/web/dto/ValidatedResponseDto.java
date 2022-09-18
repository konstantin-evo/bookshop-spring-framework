package com.example.bookshop.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Dto object used to interact with Front-End to display errors
 * <p>
 * boolean "validated" returns "true" if the data is error-free
 * and the operation is completed successfully
 * Map<String, String> contains error information (if any)
 */
@Getter
@Setter
@AllArgsConstructor
public class ValidatedResponseDto {
    private boolean validated;
    private Map<String, String> errorMessages;
}

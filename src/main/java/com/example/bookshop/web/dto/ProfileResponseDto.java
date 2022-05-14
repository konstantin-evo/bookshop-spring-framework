package com.example.bookshop.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Dto containing information about whether the data
 * in the User Profile has been changed
 *
 *  validated returns true if the data was successfully saved
 *  Map<String, String> contains error information (if any)
 */
@Getter
@Setter
@AllArgsConstructor
public class ProfileResponseDto {
    private boolean validated;
    private Map<String, String> errorMessages;
}

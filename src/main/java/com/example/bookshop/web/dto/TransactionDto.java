package com.example.bookshop.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDto {
    private String time;
    private String value;
    private String description;
}

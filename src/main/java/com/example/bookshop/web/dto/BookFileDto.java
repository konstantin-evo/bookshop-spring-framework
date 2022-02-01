package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookFileDto {
    private String hash;
    private String path;
    private String fileType;
}

package com.example.bookshop.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class TagDto {

    private Integer id;
    private String name;
    private String htmlTag;

}

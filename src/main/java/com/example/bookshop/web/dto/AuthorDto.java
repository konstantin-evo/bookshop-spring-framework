package com.example.bookshop.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "DTO Object for a person who writes book(s)")
public class AuthorDto {
    @ApiModelProperty(
            value = "family name of the author",
            dataType = "String",
            example = "Dostoevsky",
            position = 1)
    private String lastName;

    @ApiModelProperty(
            value = "personal name of the author",
            dataType = "String",
            example = "Fyodor",
            position = 2)
    private String firstName;
}

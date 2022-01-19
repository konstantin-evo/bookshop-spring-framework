package com.example.bookshop.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "DTO Object for book entity")
public class BookDto {

    @ApiModelProperty(
            value = "uniq identifier",
            dataType = "integer",
            example = "346",
            position = 1)
    private Integer id;

    @ApiModelProperty(position = 2)
    private AuthorDto author;

    @ApiModelProperty(
            value = "the name of a book",
            dataType = "String",
            example = "Crime and Punishment",
            position = 3)
    private String title;

    @ApiModelProperty(value = "old price",position = 4)
    private String priceOld;

    @ApiModelProperty(
            value = "book price at the moment without discounts and so on",
            dataType = "String",
            example = "1250",
            position = 5)
    private String price;
}

package com.example.bookshop.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ApiModel(description = "DTO Object for book entity")
public class BookDto {

    @ApiModelProperty(
            value = "uniq identifier",
            dataType = "integer",
            example = "346")
    private Integer id;

    @ApiModelProperty(
            value = "the name of a book",
            dataType = "String",
            example = "Crime and Punishment")
    private String title;

    @ApiModelProperty(
            value = "book description text",
            example = "Raskolnikov, a destitute and desperate former student, wanders through the slums of St Petersburg and commits a random murder without remorse or regret. He imagines himself to be a great man, a Napoleon: acting for a higher purpose beyond conventional moral law.")
    private String description;

    @ApiModelProperty(
            value = "date of book publication",
            example = "1866-03-20")
    private LocalDate pubDate;

    @ApiModelProperty(
            value = "if isBestseller = 1 so the book is considered to be bestseller and if 0 the book is not a " +
            "bestseller",
            example = "1")
    private Integer isBestseller;

    @ApiModelProperty(
            value = "if isActive = 1 so the book is considered to be active (possible to possibly buy, etc.)",
            example = "1")
    private Integer isActive;

    @ApiModelProperty(
            value = "book price with discounts",
            example = "1110 ₽.")
    private String priceOld;

    @ApiModelProperty(
            value = "book price at the moment without discounts and so on",
            dataType = "String",
            example = "1250 ₽.")
    private String price;

    @ApiModelProperty(
            value = "rating of the book",
            example = "1280")
    private Integer rating;

    @ApiModelProperty(
            value = "tags added to the book",
            example = "modern, psychological prose")
    private Set<TagDto> tags;

    @ApiModelProperty(
            value = "tags added to the book",
            example = "modern, psychological prose")
    private List<BookFileDto> fileList;

    @ApiModelProperty(
            value = "mnemonic identity sequence of characters",
            hidden = true)
    private String slug;

    @ApiModelProperty(
            value = "book url",
            hidden = true)
    private String image;

    @ApiModelProperty(
            value = "discount value for book",
            hidden = true)
    private Integer discount;

    @ApiModelProperty()
    private AuthorDto author;
}

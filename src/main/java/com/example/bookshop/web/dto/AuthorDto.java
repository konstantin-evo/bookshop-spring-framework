package com.example.bookshop.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@ApiModel(description = "DTO Object for a person who writes book(s)")
public class AuthorDto {

    @ApiModelProperty(
            value = "uniq identifier",
            dataType = "integer",
            example = "346",
            position = 1)
    private Integer id;

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

    @ApiModelProperty(
            value = "photo url",
            hidden = true)
    private String photo;

    @ApiModelProperty(
            value = "author's life story",
            dataType = "String",
            example = "Dostoevsky's paternal ancestors were part of a noble family of Russian Orthodox Christians. The family traced its roots back to Danilo Irtishch, who was granted lands in the Pinsk region (for centuries part of the Grand Duchy of Lithuania, now in modern-day Belarus) in 1509 for his services under a local prince, his progeny then taking the name \"Dostoevsky\" based on a village there called Dostoïevo (derived from Old Polish dostojnik – dignitary).")
    private String description;
}

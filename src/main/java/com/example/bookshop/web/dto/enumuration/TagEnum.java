package com.example.bookshop.web.dto.enumuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TagEnum {
    XS("Tag_xs"),
    SM("Tag_sm"),
    MD("Tag_md"),
    LG("Tag_lg");

    private final String name;
}

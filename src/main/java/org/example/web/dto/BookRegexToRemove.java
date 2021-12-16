package org.example.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class BookRegexToRemove {

    @NotEmpty
    private String param;

}

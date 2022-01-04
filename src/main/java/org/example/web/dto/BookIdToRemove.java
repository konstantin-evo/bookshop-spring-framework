package org.example.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class BookIdToRemove {

    @NotNull
    private Integer id;

}

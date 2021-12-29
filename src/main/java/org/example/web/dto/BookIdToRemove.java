package org.example.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookIdToRemove {

    @NotNull
    private Integer id;

}

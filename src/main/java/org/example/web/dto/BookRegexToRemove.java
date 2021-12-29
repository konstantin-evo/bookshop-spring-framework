package org.example.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookRegexToRemove {

    @NotEmpty
    @Pattern(regexp="(?i)(^)(author=|title=|size=)")
    private String param;

}

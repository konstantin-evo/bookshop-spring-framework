package org.example.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookRegexToRemove {

    @NotEmpty
    @Pattern(regexp="(?i)(^)(author=|title=|size=)")
    private String param;

}

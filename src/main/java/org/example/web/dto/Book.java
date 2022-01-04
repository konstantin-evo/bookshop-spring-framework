package org.example.web.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Book {

    @NotNull
    private Integer id;
    @Size(min = 1, max = 250)
    @NotNull
    @Pattern(regexp="^\\p{L}+[\\p{L}\\p{Pd}\\p{Zs}']*\\p{L}+$|^\\p{L}+$")
    private String author;
    @Size(min = 1, max = 250)
    @NotNull
    private String title;
    @Digits(integer = 4, fraction = 0)
    @Size(min = 1)
    private Integer size;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }
}
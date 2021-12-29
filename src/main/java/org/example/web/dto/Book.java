package org.example.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
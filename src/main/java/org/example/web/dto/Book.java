package org.example.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;

@Getter @Setter
public class Book {

    private String id;
    private String author;
    private String title;
    @Digits(integer = 4, fraction = 0)
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

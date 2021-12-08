package org.example.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Book {

    private Integer id;
    private String author;
    private String title;
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

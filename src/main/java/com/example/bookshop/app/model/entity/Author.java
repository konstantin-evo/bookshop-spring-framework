package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Author {

    private Integer id;
    private String surname;
    private String name;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}

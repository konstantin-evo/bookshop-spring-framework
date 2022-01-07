package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Book {

    private Integer id;
    private Integer authorId;
    private String title;
    private String priceOld;
    private String price;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author_id='" + authorId + '\'' +
                ", title='" + title + '\'' +
                ", priceOld=" + priceOld +
                ", price=" + price +
                '}';
    }
}

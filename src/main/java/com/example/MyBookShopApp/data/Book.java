package com.example.MyBookShopApp.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Book {

    private Integer id;
    private String author;
    private String title;
    private String priceOld;
    private String price;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", priceOld=" + priceOld +
                ", price=" + price +
                '}';
    }
}

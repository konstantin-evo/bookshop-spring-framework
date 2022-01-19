package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookDto;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@org.mapstruct.Mapper
public interface  Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    @Mapping(target = "priceOld", source =  ".", qualifiedByName = "calculatePriceOld")
    BookDto map(Book book);
    List<BookDto> map(List<Book> books);
    AuthorDto map(Author author);

    @Named("calculatePriceOld")
    static String calculatePriceOld(Book book) {
        double discount = book.getDiscount()/100;
        return String.format("%.2f", book.getPrice()/(1-discount))
                .replace(',', '.')
                .replace(".00","");
    }

}

package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.TagDto;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.mapstruct.Mapper
public interface  Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    @Mapping(target = "priceOld", source =  ".", qualifiedByName = "calculatePriceOld")
    @Mapping(target = "tags", source =  ".", qualifiedByName = "getTags")
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

    @Named("getTags")
    static Set<TagDto> getTags(Book book) {
        return book.getBookToTag().stream()
                .map(bookToTag -> {
                    TagDto tag = new TagDto();
                    tag.setId(bookToTag.getTag().getId());
                    tag.setName(bookToTag.getTag().getName());
                    return tag;
                }).collect(Collectors.toSet());
    }

    default Integer convertToInteger(Double rating){
        return rating.intValue();
    }

    default LocalDate convertToLocalDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    /**
     * This stub method is necessary due to the fact that
     * the date when changing the calendar on the /books/recent page
     * comes in the format DD.MM.YYYY and can't be parsed in this form
     *
     * @param stringDate - data from the "/books/recent" page, after JavaScript processing
     * @return LocalDate for BootDto "pubDate" field
     */
    default LocalDate convertToLocalDate(String stringDate) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(stringDate);
        } catch (DateTimeParseException e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            localDate = LocalDate.parse(stringDate.replace(".", "-"), formatter);
        }
        return localDate;
    }

}

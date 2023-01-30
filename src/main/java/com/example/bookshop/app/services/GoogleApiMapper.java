package com.example.bookshop.app.services;

import com.example.bookshop.app.model.google.api.books.Item;
import com.example.bookshop.web.dto.BookGoogleDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Objects;

/**
 * Mapper to Convert JSON from GoogleBook API to POJO Java Object
 *
 * The data for the actual books is inside the `items` data field
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface GoogleApiMapper {

    @Mapping(target = "author", source = ".", qualifiedByName = "getAuthor")
    @Mapping(target = "price", source = ".", qualifiedByName = "getPrice")
    @Mapping(target = "title", source = "volumeInfo.title")
    @Mapping(target = "description", source = "volumeInfo.description")
    @Mapping(target = "image", source = "volumeInfo.imageLinks.thumbnail")
    @Mapping(target = "link", source = "volumeInfo.previewLink")
    BookGoogleDto mapGoogleBookToEntity(Item googleItem);

    @Named("getAuthor")
    static String getAuthor(Item item) {
        return (item.getVolumeInfo().getAuthors() != null)
                ? item.getVolumeInfo().getAuthors().get(0)
                : "Publisher is " + item.getVolumeInfo().getPublisher() + "(no author for this book)";
    }

    @Named("getPrice")
    static String getPrice(Item item) {
        return (!Objects.equals(item.getSaleInfo().getSaleability(), "NOT_FOR_SALE"))
                ? String.valueOf(item.getSaleInfo().getListPrice().getAmount())
                : "Not for sale";
    }

}

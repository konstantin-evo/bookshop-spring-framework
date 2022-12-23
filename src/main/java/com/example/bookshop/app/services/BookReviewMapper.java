package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.web.dto.UserReviewDto;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@org.mapstruct.Mapper
public interface BookReviewMapper {

    BookReviewMapper INSTANCE = Mappers.getMapper(BookReviewMapper.class);

    @Mapping(target = "text", source = "review.text")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "bookSlug", source = "book.slug")
    UserReviewDto mapToDto(BookRate bookRate);

    List<UserReviewDto> map(List<BookRate> bookRates);
}

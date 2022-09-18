package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.BookReview;
import com.example.bookshop.app.model.entity.BookToFile;
import com.example.bookshop.app.model.entity.FileType;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookCreateDto;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.BookFileDto;
import com.example.bookshop.web.dto.BookRateDto;
import com.example.bookshop.web.dto.ReviewDto;
import com.example.bookshop.web.dto.TagDto;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@org.mapstruct.Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "priceOld", source = ".", qualifiedByName = "calculatePriceOld")
    @Mapping(target = "tags", source = ".", qualifiedByName = "getTags")
    BookDto map(Book book);

    @Mapping(target = "author", ignore = true)
    Book map(BookCreateDto bookDto);

    @Mapping(target = "rate", source = ".", qualifiedByName = "getBookRate")
    @Mapping(target = "rateDistribution", source = ".", qualifiedByName = "getRateDistribution")
    BookRateDto mapBookRateDto(Book book);

    List<BookDto> map(List<Book> books);

    AuthorDto map(Author author);

    BookFileDto map(BookToFile file);

    @Named("calculatePriceOld")
    static String calculatePriceOld(Book book) {
        if (book.getDiscount() != null) {
            double discount = book.getDiscount() / 100;
            return String.valueOf((int) (book.getPrice() / (1 - discount)));
        } else {
            return null;
        }
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

    /**
     * The method returns the Rating of the Book based on the Users' assessment
     *
     * @return an integer from 1 to 5 that represents the rating of the book (rounded up)
     * if the book has no ratings, returns 5 (maximum rating)
     */
    @Named("getBookRate")
    static Integer getBookRate(Book book) {
        if (book.getBookRates().isEmpty()) {
            return 5;
        } else {
            double sum = book.getBookRates().stream().mapToDouble(BookRate::getRating).sum();
            return (int) Math.ceil(sum / book.getBookRates().size());
        }
    }

    /**
     * The method returns the distribution of User ratings for the Book
     *
     * @return Map<Integer, Integer> which contains the key "Rating" from 1 to 5
     * and the value "Count", which shows the number of users who gave this rating
     */
    @Named("getRateDistribution")
    static Map<Integer, Integer> getRateDistribution(Book book) {
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> list = book.getBookRates().stream()
                .mapToInt(BookRate::getRating)
                .boxed().collect(Collectors.toList());

        for (int i = 1; i <= 5; i++) {
            int frequency = Collections.frequency(list, i);
            map.put(i, frequency);
        }
        return map;
    }

    default ReviewDto getBookReviews(Book book) {

        List<ReviewDto.Review> reviews = book.getBookRates().stream()
                .filter(bookRate -> bookRate.getReview() != null)
                .map(bookRate -> ReviewDto.Review.builder()
                        .id(bookRate.getReview().getId())
                        .rate(bookRate.getRating())
                        .text(bookRate.getReview().getText())
                        .userName(bookRate.getUser().getName())
                        .pubDate(new SimpleDateFormat("MM.dd.yyyy HH:mm")
                                .format(bookRate.getReview().getPubDate()))
                        .likes(countReviewRate(bookRate.getReview(), 1))
                        .dislikes(countReviewRate(bookRate.getReview(), -1))
                        .build()
                ).collect(Collectors.toList());

        Integer totalReviews = (int) book.getBookRates().stream()
                .filter(bookRate -> bookRate.getReview() != null).count();

        return ReviewDto.builder()
                .reviews(reviews)
                .totalReviews(totalReviews)
                .build();
    }

    default Integer convertToInteger(Double rating) {
        return rating.intValue();
    }

    default LocalDate convertToLocalDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    default String convertBookFileType(FileType type) {
        return String.valueOf(type.getName().getFileExtensionString());
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

    default Integer countReviewRate(BookReview review, Integer value) {
        return (int) review.getReviewRates().stream()
                .filter(bookReviewRate -> Objects.equals(bookReviewRate.getRate(), value))
                .count();
    }

}

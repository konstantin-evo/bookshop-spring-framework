package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.GoogleApiService;
import com.example.bookshop.web.dto.ApiResponse;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.BooksPageGoogleDto;
import com.example.bookshop.web.dto.BooksPageDto;
import com.example.bookshop.web.exception.BookstoreApiWrongParameterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "book data api")
public class BooksRestApiController {

    private final BookService bookService;
    private final GoogleApiService googleService;

    public BooksRestApiController(BookService bookService, GoogleApiService googleService) {
        this.bookService = bookService;
        this.googleService = googleService;
    }

    @GetMapping("/books/by-author")
    @ApiOperation("operation to get book list of bookshop by passed author first name")
    public ResponseEntity<List<BookDto>> booksByAuthor(@RequestParam("author") String authorName) {
        return ResponseEntity.ok(bookService.getBooksByAuthorName(authorName));
    }

    @GetMapping("/books/by-title")
    @ApiOperation("get books by title")
    public ResponseEntity<ApiResponse<BookDto>> booksByTitle(@RequestParam("title") String title)
            throws BookstoreApiWrongParameterException {
        List<BookDto> data = bookService.getBooksByTitle(title);
        ApiResponse<BookDto> response = new ApiResponse<>(
                HttpStatus.OK,
                LocalDateTime.now(),
                "data size: " + data.size() + " elements",
                "successful request",
                data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/by-price")
    @ApiOperation("get books by price")
    public ResponseEntity<List<BookDto>> booksByPrice(@RequestParam("price") Integer price) {
        return ResponseEntity.ok(bookService.getBooksWithPrice(price));
    }

    @GetMapping("/books/by-discount-range")
    @ApiOperation("get books by discount range from min to max discount")
    public ResponseEntity<List<BookDto>> discountRangeBooks(@RequestParam("min") Double min,
                                                            @RequestParam("max") Double max) {
        return ResponseEntity.ok(bookService.getBooksWithDiscountBetween(min, max));
    }

    @GetMapping("/books/by-price-range")
    @ApiOperation("get books by price range from min to max price")
    public ResponseEntity<List<BookDto>> priceRangeBooks(@RequestParam("min") Integer min,
                                                         @RequestParam("max") Integer max) {
        return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
    }

    @GetMapping("/books/with-max-discount")
    @ApiOperation("get list of book with max price")
    public ResponseEntity<List<BookDto>> maxPriceBooks() {
        return ResponseEntity.ok(bookService.getBooksWithMaxPrice());
    }

    @GetMapping("/books/bestsellers")
    @ApiOperation("get bestseller book (which is_bestseller = 1)")
    public ResponseEntity<List<BookDto>> bestSellerBooks() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }

    @GetMapping("/books/recent")
    @ResponseBody
    public BooksPageDto getRecentBooksPage(@RequestParam("from") String from,
                                           @RequestParam("to") String to,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {

        LocalDate dateFrom = bookService.convertToLocalDate(from);
        LocalDate dateTo = bookService.convertToLocalDate(to);

        return new BooksPageDto(bookService.getPageOfRecentBooks(dateFrom, dateTo, offset, limit)
                .getContent());
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getPageOfRecommendedBooks(offset, limit)
                        .getContent());
    }

    @GetMapping("/books/popular")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getPageOfPopularBooks(offset, limit)
                        .getContent());
    }

    @GetMapping("/books/tags/{id}")
    @ResponseBody
    public BooksPageDto getBooksByTag(@PathVariable Integer id,
                                      @RequestParam("offset") Integer offset,
                                      @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getPageOfBooksByTag(offset, limit, id)
                        .getContent());
    }

    @GetMapping("/books/genre/{id}")
    @ResponseBody
    public BooksPageDto getBooksByGenre(@PathVariable Integer id,
                                        @RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getPageOfBooksByGenre(offset, limit, id)
                        .getContent());
    }

    @GetMapping("/books/author/{id}")
    @ResponseBody
    public BooksPageDto getBooksByAuthor(@PathVariable Integer id,
                                         @RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getBooksByAuthorId(offset, limit, id)
                        .getContent());
    }

    @GetMapping("/search/{searchWord}")
    @ResponseBody
    public BooksPageGoogleDto getBooksBySearchQuery(@PathVariable String searchWord,
                                                    @RequestParam("offset") Integer offset,
                                                    @RequestParam("limit") Integer limit) {
        return new BooksPageGoogleDto(googleService
                .getPageOfSearchResult(searchWord, offset, limit));
    }

}

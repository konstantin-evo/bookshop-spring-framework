package com.example.bookshop.web.controllers.rest;

import com.example.bookshop.app.services.AdminService;
import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.ApiResponse;
import com.example.bookshop.web.dto.BookCreateDto;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.BooksPageDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import com.example.bookshop.web.exception.BookstoreApiWrongParameterException;
import com.example.bookshop.web.services.ResourceStorage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Api(value = "book data api")
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;
    private final AdminService adminService;
    private final ResourceStorage storage;

    @GetMapping("/by-author")
    @ApiOperation("operation to get book list of bookshop by passed author first name")
    public ResponseEntity<List<BookDto>> booksByAuthor(@RequestParam("author") String authorName) {
        return ResponseEntity.ok(bookService.getBooksByAuthorName(authorName));
    }

    @GetMapping("/by-title")
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

    @GetMapping("/by-price")
    @ApiOperation("get books by price")
    public ResponseEntity<List<BookDto>> booksByPrice(@RequestParam("price") Integer price) {
        return ResponseEntity.ok(bookService.getBooksWithPrice(price));
    }

    @GetMapping("/by-discount-range")
    @ApiOperation("get books by discount range from min to max discount")
    public ResponseEntity<List<BookDto>> discountRangeBooks(@RequestParam("min") Double min,
                                                            @RequestParam("max") Double max) {
        return ResponseEntity.ok(bookService.getBooksWithDiscountBetween(min, max));
    }

    @GetMapping("/by-price-range")
    @ApiOperation("get books by price range from min to max price")
    public ResponseEntity<List<BookDto>> priceRangeBooks(@RequestParam("min") Integer min,
                                                         @RequestParam("max") Integer max) {
        return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
    }

    @GetMapping("/with-max-discount")
    @ApiOperation("get list of book with max price")
    public ResponseEntity<List<BookDto>> maxPriceBooks() {
        return ResponseEntity.ok(bookService.getBooksWithMaxPrice());
    }

    @GetMapping("/bestsellers")
    @ApiOperation("get bestseller book (which is_bestseller = 1)")
    public ResponseEntity<List<BookDto>> bestSellerBooks() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }

    @GetMapping("/recent")
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

    @GetMapping("/recommended")
    @ResponseBody
    public BooksPageDto getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getPageOfRecommendedBooks(offset, limit)
                        .getContent());
    }

    @GetMapping("/popular")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getPageOfPopularBooks(offset, limit)
                        .getContent());
    }

    @GetMapping("/{slug}")
    @ResponseBody
    public BookDto getBooksByTag(@PathVariable String slug) {
        return bookService.getBook(slug);
    }

    @GetMapping("/tags/{id}")
    @ResponseBody
    public BooksPageDto getBooksByTag(@PathVariable Integer id,
                                      @RequestParam("offset") Integer offset,
                                      @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getPageOfBooksByTag(offset, limit, id)
                        .getContent());
    }

    @GetMapping("/genre/{id}")
    @ResponseBody
    public BooksPageDto getBooksByGenre(@PathVariable Integer id,
                                        @RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getPageOfBooksByGenre(offset, limit, id)
                        .getContent());
    }

    @GetMapping("/author/{id}")
    @ResponseBody
    public BooksPageDto getBooksByAuthor(@PathVariable Integer id,
                                         @RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit) {
        return new BooksPageDto(
                bookService
                        .getBooksByAuthorId(offset, limit, id)
                        .getContent());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidatedResponseDto createBook(@RequestBody BookCreateDto bookDto) {
        return adminService.createBook(bookDto);
    }

    @PatchMapping(value = "/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidatedResponseDto editBook(@RequestBody BookCreateDto bookDto, @PathVariable String slug) {
        return adminService.editBook(bookDto, slug);
    }

    @DeleteMapping(value = "/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidatedResponseDto deleteBook(@PathVariable String slug) {
        return adminService.deleteBook(slug);
    }

    @PostMapping("/{slug}/img/save")
    @ResponseBody
    public ValidatedResponseDto saveNewBookImage(@RequestParam("file") MultipartFile file,
                                                 @PathVariable("slug") String slug) throws IOException {
        String path = storage.saveNewBookCover(file, slug);
        bookService.updateBook(slug, path);
        return new ValidatedResponseDto(true, null);
    }

}

package com.example.bookshop.web.controllers;

import com.example.bookshop.app.services.BookService;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.services.ResourceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BookPagesController {

    private final BookService bookService;
    private final ResourceStorage storage;

    private static final int OFFSET = 0;
    private static final int LIMIT = 6;
    private static final int DEFAULT_RECENT_MONTH = 6;

    @Autowired
    public BookPagesController(BookService bookService, ResourceStorage storage) {
        this.bookService = bookService;
        this.storage = storage;
    }

    @GetMapping("/popular")
    public String popularPage(Model model) {
        model.addAttribute("popularBooks", bookService
                .getPageOfPopularBooks(OFFSET, LIMIT)
                .getContent());
        return "books/popular";
    }

    @GetMapping("/recent")
    public String recentPage(Model model) {
        model.addAttribute("recentBooks", recentBooks());
        return "books/recent";
    }

    @GetMapping("/{slug}")
    public String bookSlugPage(@PathVariable String slug, Model model) {
        model.addAttribute("book", bookService.getBook(slug));
        return "books/slug";
    }

    private List<BookDto> recentBooks() {
        LocalDate dateTo = LocalDate.now();
        LocalDate dateFrom = LocalDate.now().minusMonths(DEFAULT_RECENT_MONTH);
        return bookService
                .getPageOfRecentBooks(dateFrom, dateTo, OFFSET, LIMIT)
                .getContent();
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file,
                                   @PathVariable("slug") String slug) throws IOException {
        String path = storage.saveNewBookCover(file, slug);
        bookService.updateBook(slug, path);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash")String hash) throws IOException{
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: "+path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: "+mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: "+data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

}

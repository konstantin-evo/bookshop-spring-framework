package com.example.bookshop.web.controllers.book;

import com.example.bookshop.app.services.BookRateService;
import com.example.bookshop.app.services.BookService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BookPageController {

    private final BookService bookService;
    private final BookRateService rateService;
    private final ResourceStorage storage;

    @Autowired
    public BookPageController(BookService bookService, ResourceStorage storage, BookRateService rateService) {
        this.bookService = bookService;
        this.storage = storage;
        this.rateService = rateService;
    }

    @GetMapping("/{slug}")
    public String mainBookPage(@PathVariable String slug, Model model) {
        model.addAttribute("book", bookService.getBook(slug));
        model.addAttribute("bookRate", bookService.getBookRate(slug));
        //TODO: Refactor after module "8. Security of Spring Applications"
        // User credential should be taken from the session
        model.addAttribute("userRate", rateService.getUserRate(slug, 88));
        model.addAttribute("reviews", bookService.getBookReviews(slug));
        return "books/slug";
    }

    @PostMapping("/rateBook/{slug}")
    public String handleRatingBook(@PathVariable("slug") String slug,
                                   @RequestBody String userRate) {
        //TODO: Refactor after module "8. Security of Spring Applications"
        // User credential should be taken from the session
        rateService.setBookRate(slug, userRate, 88);
        return "redirect:/books/" + slug;
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file,
                                   @PathVariable("slug") String slug) throws IOException {
        String path = storage.saveNewBookCover(file, slug);
        bookService.updateBook(slug, path);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

}

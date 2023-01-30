package com.example.bookshop.app.services;

import com.example.bookshop.app.model.google.api.books.Root;
import com.example.bookshop.web.dto.BookGoogleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for interaction with GoogleBook API
 */
@Service
@RequiredArgsConstructor
public class GoogleApiService {

    private final WebClient webClient;
    private final GoogleApiMapper googleApiMapper;

    @Value("${google.books.api.key}")
    private String apiKey;

    public List<BookGoogleDto> getPageOfSearchResult(String searchWord, Integer offset, Integer limit) {

        Root root = getRootElement(searchWord, offset, limit);

        return (root == null || root.getItems().isEmpty())
                ? Collections.emptyList()
                : root.getItems().stream().map(googleApiMapper::mapGoogleBookToEntity).collect(Collectors.toList());
    }

    private Root getRootElement(String searchWord, Integer offset, Integer limit) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("v1/volumes/")
                        .queryParam("q", searchWord)
                        .queryParam("key", apiKey)
                        .queryParam("filter", "paid-ebooks")
                        .queryParam("startIndex", offset)
                        .queryParam("maxResult", limit)
                        .build())
                .retrieve()
                .bodyToMono(Root.class)
                .block();
    }
}

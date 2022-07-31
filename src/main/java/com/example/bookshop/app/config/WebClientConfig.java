package com.example.bookshop.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * WebClient Configuration for interaction with GoogleBook API
 */
@Configuration
public class WebClientConfig {

    @Value("${google.books.api.url}")
    private String GOOGLE_URL;

    @Value("${google.books.api.timeout}")
    private int TIMEOUT;

    @Bean
    public WebClient webClientGoogle() {

        final HttpClient client = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(TIMEOUT));

        return org.springframework.web.reactive.function.client.WebClient.builder()
                .baseUrl(GOOGLE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultCookie("SameSite", "None")
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }
}

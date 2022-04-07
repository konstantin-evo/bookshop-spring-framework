package com.example.bookshop.app.model.google.api.books;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Designed to interact with GoogleBook API
 *
 * Search result information related to this volume.
 */
public class SearchInfo {
    @JsonProperty("textSnippet")
    public String getTextSnippet() {
        return this.textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }

    String textSnippet;
}

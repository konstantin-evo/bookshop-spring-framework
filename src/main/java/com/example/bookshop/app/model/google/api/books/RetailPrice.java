package com.example.bookshop.app.model.google.api.books;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Designed to interact with GoogleBook API
 *
 * The actual selling price of the book.
 * This is the same as the suggested retail or list price unless there are offers or discounts on this volume.
 */
public class RetailPrice {
    @JsonProperty("amount")
    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    double amount;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    String currencyCode;

    @JsonProperty("amountInMicros")
    public double getAmountInMicros() {
        return this.amountInMicros;
    }

    public void setAmountInMicros(double amountInMicros) {
        this.amountInMicros = amountInMicros;
    }

    double amountInMicros;
}

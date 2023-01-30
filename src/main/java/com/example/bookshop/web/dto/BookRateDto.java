package com.example.bookshop.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

// Using for display Rate information on Book page
@Getter
@Builder
public class BookRateDto {
    private Integer rate;
    private Map<Integer, Integer> rateDistribution;
}

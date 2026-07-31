package com.wandersync.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ItineraryRequest {
    @NotBlank
    private String destination;

    @Min(1)
    private int durationDays;

    private String budget; // Luxury, Moderate, Backpacker

    private List<String> preferences; // Culture, Nature, Food, Adventure
}

package com.wandersync.backend.service;

import com.wandersync.backend.dto.QuoteResponse;
import com.wandersync.backend.model.Booking;
import com.wandersync.backend.model.ScheduledTrip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PricingService {

    private static final BigDecimal DEFAULT_MULTIPLIER = BigDecimal.ONE;
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    public QuoteResponse calculateQuote(ScheduledTrip trip,
                                        List<String> selectedSeatIds,
                                        List<String> selectedAddOnIds) {
        if (selectedSeatIds == null || selectedSeatIds.isEmpty()) {
            throw new IllegalArgumentException("At least one seat ID must be selected");
        }
        BigDecimal multiplier = money(trip.getDynamicYieldMultiplier() == null
                ? DEFAULT_MULTIPLIER
                : trip.getDynamicYieldMultiplier());
        List<ScheduledTrip.Seat> tripSeats = trip.getSeatMap() == null ? List.of() : trip.getSeatMap();
        List<ScheduledTrip.AddOn> tripAddOns = trip.getAvailableAddons() == null
                ? List.of()
                : trip.getAvailableAddons();

        Set<String> requestedSeatIds = new HashSet<>();
        List<QuoteResponse.SeatLineItem> seatItems = selectedSeatIds.stream()
                .map(seatId -> {
                    String normalizedId = normalizeId(seatId, "seat");
                    if (!requestedSeatIds.add(normalizedId)) {
                        throw new IllegalArgumentException("Duplicate seat ID: " + seatId);
                    }

                    ScheduledTrip.Seat seat = tripSeats.stream()
                            .filter(candidate -> candidate.getSeatId() != null
                                    && candidate.getSeatId().equalsIgnoreCase(seatId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Seat ID " + seatId + " not found in trip " + trip.getId()));
                    BigDecimal seatPrice = requirePrice(seat.getPrice(), "seat " + seatId);
                    BigDecimal total = money(seatPrice.multiply(multiplier));
                    return QuoteResponse.SeatLineItem.builder()
                            .seatId(seat.getSeatId())
                            .seatPrice(seatPrice)
                            .multiplier(multiplier)
                            .total(total)
                            .build();
                })
                .toList();

        Set<String> requestedAddOnIds = new HashSet<>();
        List<QuoteResponse.AddOnLineItem> addOnItems = selectedAddOnIds == null
                ? List.of()
                : selectedAddOnIds.stream()
                        .map(addOnId -> {
                            String normalizedId = normalizeId(addOnId, "add-on");
                            if (!requestedAddOnIds.add(normalizedId)) {
                                throw new IllegalArgumentException("Duplicate add-on ID: " + addOnId);
                            }

                            ScheduledTrip.AddOn addOn = tripAddOns.stream()
                                    .filter(candidate -> candidate.getId() != null
                                            && candidate.getId().equalsIgnoreCase(addOnId))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException(
                                            "Add-on ID " + addOnId + " not found in trip " + trip.getId()));
                            return QuoteResponse.AddOnLineItem.builder()
                                    .addOnId(addOn.getId())
                                    .name(addOn.getName())
                                    .price(requirePrice(addOn.getPrice(), "add-on " + addOnId))
                                    .build();
                        })
                        .toList();

        BigDecimal baseSeatsSubtotal = money(seatItems.stream()
                .map(QuoteResponse.SeatLineItem::getTotal)
                .reduce(ZERO, BigDecimal::add));
        BigDecimal addOnsSubtotal = money(addOnItems.stream()
                .map(QuoteResponse.AddOnLineItem::getPrice)
                .reduce(ZERO, BigDecimal::add));
        BigDecimal grandTotal = money(baseSeatsSubtotal.add(addOnsSubtotal));

        Booking.PriceBreakdown breakdown = Booking.PriceBreakdown.builder()
                .baseSeatsSubtotal(baseSeatsSubtotal)
                .addOnsSubtotal(addOnsSubtotal)
                .dynamicYieldMultiplier(multiplier)
                .grandTotal(grandTotal)
                .currency("INR")
                .build();

        return QuoteResponse.builder()
                .tripId(trip.getId())
                .seatItems(seatItems)
                .addOnItems(addOnItems)
                .priceBreakdown(breakdown)
                .build();
    }

    private String normalizeId(String id, String itemType) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Selected " + itemType + " ID must not be blank");
        }
        return id.toLowerCase(Locale.ROOT);
    }

    private BigDecimal requirePrice(BigDecimal price, String itemDescription) {
        if (price == null) {
            throw new IllegalArgumentException("Missing price for " + itemDescription);
        }
        return money(price);
    }

    private BigDecimal money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}

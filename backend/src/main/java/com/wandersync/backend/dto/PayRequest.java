package com.wandersync.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// userId and amount are intentionally absent — the payer is the
// authenticated user and the amount charged is the lobby's
// sharePerMember, verified against the payment provider server-side.
@Data
public class PayRequest {
    // Reference returned by the payment gateway (e.g. Razorpay/Stripe
    // payment id) that the backend verifies before marking PAID.
    @NotBlank
    private String paymentReference;
}

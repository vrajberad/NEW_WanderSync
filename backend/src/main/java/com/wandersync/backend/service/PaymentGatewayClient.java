package com.wandersync.backend.service;

import java.math.BigDecimal;

/**
 * Abstraction over the real payment provider (Razorpay, Stripe, ...).
 * Implement this against the provider's server-side verification API:
 * given a payment reference/id created by the frontend checkout, fetch
 * the payment from the provider and return the captured amount.
 * Throw if the payment does not exist, failed, or was not captured.
 */
public interface PaymentGatewayClient {

    BigDecimal verifyPayment(String paymentReference);

    GatewayOrder createOrder(OrderRequest request);

    boolean verifyWebhookSignature(String payload, String signature);

    record OrderRequest(
            BigDecimal amount,
            String currency,
            String receipt,
            String idempotencyKey) {
    }

    record GatewayOrder(
            String orderId,
            BigDecimal amount,
            String currency,
            String keyId) {
    }
}

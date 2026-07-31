package com.wandersync.backend.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentOrderResponse {

    String orderId;
    String keyId;
    long amountInPaise;
    String currency;
    String bookingId;
    String paymentId;
}

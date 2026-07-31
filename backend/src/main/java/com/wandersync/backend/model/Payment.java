package com.wandersync.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @Indexed
    private String bookingId;

    private String payerUserId;

    private Gateway gateway;

    @Indexed
    private String gatewayOrderId;

    private String gatewayPaymentId;

    private BigDecimal amount;

    private String currency;

    private Status status;

    @Indexed(unique = true)
    private String idempotencyKey;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public enum Gateway {
        RAZORPAY
    }

    public enum Status {
        CREATED,
        CAPTURED,
        FAILED,
        REFUNDED
    }
}

package com.wandersync.backend.service;

import com.wandersync.backend.config.RazorpayProperties;
import com.wandersync.backend.dto.PaymentOrderResponse;
import com.wandersync.backend.exception.ResourceNotFoundException;
import com.wandersync.backend.exception.WebhookVerificationException;
import com.wandersync.backend.model.Booking;
import com.wandersync.backend.model.Payment;
import com.wandersync.backend.repository.BookingRepository;
import com.wandersync.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final PaymentGatewayClient paymentGatewayClient;
    private final RazorpayProperties razorpayProperties;

    public PaymentOrderResponse createOrderForBooking(String bookingId, String userId) {
        Booking booking = findOwnedBooking(bookingId, userId);
        if (booking.getStatus() != Booking.Status.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking is not awaiting payment");
        }

        String idempotencyKey = "booking:" + bookingId;
        Payment existingPayment = paymentRepository.findByIdempotencyKey(idempotencyKey).orElse(null);
        if (existingPayment != null
                && (existingPayment.getStatus() == Payment.Status.CREATED
                || existingPayment.getStatus() == Payment.Status.CAPTURED)) {
            return toResponse(existingPayment, bookingId);
        }

        BigDecimal amount = booking.getPriceBreakdown().getGrandTotal();
        String currency = resolveCurrency(booking);
        PaymentGatewayClient.GatewayOrder order = paymentGatewayClient.createOrder(
                new PaymentGatewayClient.OrderRequest(amount, currency, bookingId, idempotencyKey));
        Payment payment = Payment.builder()
                .bookingId(bookingId)
                .payerUserId(userId)
                .gateway(Payment.Gateway.RAZORPAY)
                .gatewayOrderId(order.orderId())
                .amount(amount)
                .currency(currency)
                .status(Payment.Status.CREATED)
                .idempotencyKey(idempotencyKey)
                .build();
        try {
            payment = paymentRepository.save(payment);
        } catch (DuplicateKeyException ex) {
            Payment racedPayment = paymentRepository.findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() -> ex);
            return toResponse(racedPayment, bookingId);
        }

        List<String> paymentIds = booking.getPaymentIds() == null
                ? new ArrayList<>()
                : new ArrayList<>(booking.getPaymentIds());
        paymentIds.add(payment.getId());
        booking.setPaymentIds(paymentIds);
        bookingRepository.save(booking);
        return toResponse(payment, bookingId);
    }

    public void handleWebhook(String payload, String signature) {
        if (!paymentGatewayClient.verifyWebhookSignature(payload, signature)) {
            throw new WebhookVerificationException("Webhook signature verification failed");
        }

        WebhookPayment webhookPayment = parseWebhook(payload);
        if (!"payment.captured".equals(webhookPayment.event())
                && !"order.paid".equals(webhookPayment.event())) {
            return;
        }

        List<Payment> matchingPayments = paymentRepository.findByGatewayOrderId(webhookPayment.orderId());
        Payment payment = matchingPayments.stream()
                .filter(candidate -> candidate.getStatus() == Payment.Status.CREATED)
                .findFirst()
                .orElseGet(() -> matchingPayments.size() == 1 ? matchingPayments.get(0) : null);
        if (payment == null) {
            log.warn("Received Razorpay webhook for unknown order {}", webhookPayment.orderId());
            return;
        }
        if (payment.getStatus() == Payment.Status.CAPTURED) {
            return;
        }

        long expectedPaise = payment.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
        if (webhookPayment.amountInPaise() != null
                && webhookPayment.amountInPaise() != expectedPaise) {
            log.error("Razorpay webhook amount mismatch for order {}", webhookPayment.orderId());
            throw new WebhookVerificationException("Webhook amount verification failed");
        }

        payment.setStatus(Payment.Status.CAPTURED);
        payment.setGatewayPaymentId(webhookPayment.paymentId());
        paymentRepository.save(payment);

        Booking booking = bookingRepository.findById(payment.getBookingId()).orElse(null);
        if (booking != null && booking.getStatus() == Booking.Status.PENDING_PAYMENT) {
            bookingService.confirmBooking(booking.getId());
        }
    }

    private Booking findOwnedBooking(String bookingId, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        if (!userId.equals(booking.getUserId())) {
            throw new ResourceNotFoundException("Booking not found with id: " + bookingId);
        }
        return booking;
    }

    private String resolveCurrency(Booking booking) {
        String bookingCurrency = booking.getPriceBreakdown().getCurrency();
        return bookingCurrency == null || bookingCurrency.isBlank()
                ? razorpayProperties.getCurrency()
                : bookingCurrency;
    }

    private PaymentOrderResponse toResponse(Payment payment, String bookingId) {
        return PaymentOrderResponse.builder()
                .orderId(payment.getGatewayOrderId())
                .keyId(razorpayProperties.getKeyId())
                .amountInPaise(payment.getAmount()
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(0, RoundingMode.HALF_UP)
                        .longValueExact())
                .currency(payment.getCurrency())
                .bookingId(bookingId)
                .paymentId(payment.getId())
                .build();
    }

    private WebhookPayment parseWebhook(String payload) {
        try {
            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");
            JSONObject payloadObject = json.getJSONObject("payload");
            JSONObject entity;
            String orderId;
            String paymentId = null;
            Long amount = null;
            if ("payment.captured".equals(event)) {
                entity = payloadObject.getJSONObject("payment").getJSONObject("entity");
                orderId = entity.getString("order_id");
                paymentId = entity.optString("id", null);
                amount = entity.has("amount") ? entity.getLong("amount") : null;
            } else if ("order.paid".equals(event)) {
                entity = payloadObject.getJSONObject("order").getJSONObject("entity");
                orderId = entity.getString("id");
                amount = entity.has("amount_paid")
                        ? entity.getLong("amount_paid")
                        : entity.has("amount") ? entity.getLong("amount") : null;
            } else {
                return new WebhookPayment(event, null, null, null);
            }
            return new WebhookPayment(event, orderId, paymentId, amount);
        } catch (JSONException | ArithmeticException ex) {
            throw new WebhookVerificationException("Invalid webhook payload", ex);
        }
    }

    private record WebhookPayment(
            String event,
            String orderId,
            String paymentId,
            Long amountInPaise) {
    }
}

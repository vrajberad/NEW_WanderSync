package com.wandersync.backend.service;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.wandersync.backend.config.RazorpayProperties;
import com.wandersync.backend.exception.PaymentGatewayException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RazorpayPaymentGatewayClient implements PaymentGatewayClient {

    private final RazorpayProperties properties;
    private final RazorpayClient razorpayClient;

    public RazorpayPaymentGatewayClient(RazorpayProperties properties) {
        this.properties = properties;
        try {
            this.razorpayClient = new RazorpayClient(properties.getKeyId(), properties.getKeySecret());
        } catch (RazorpayException ex) {
            throw new PaymentGatewayException("Unable to initialize Razorpay client", ex);
        }
    }

    @Override
    public GatewayOrder createOrder(OrderRequest request) {
        try {
            long amountInPaise = request.amount()
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValueExact();
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", request.currency());
            orderRequest.put("receipt", request.receipt());
            orderRequest.put("payment_capture", 1);
            com.razorpay.Order order = razorpayClient.orders.create(orderRequest);
            return new GatewayOrder(
                    order.get("id").toString(),
                    request.amount(),
                    request.currency(),
                    properties.getKeyId());
        } catch (RazorpayException | ArithmeticException ex) {
            throw new PaymentGatewayException("Unable to create Razorpay order", ex);
        }
    }

    @Override
    public boolean verifyWebhookSignature(String payload, String signature) {
        try {
            return Utils.verifyWebhookSignature(payload, signature, properties.getWebhookSecret());
        } catch (RazorpayException ex) {
            return false;
        }
    }

    @Override
    public BigDecimal verifyPayment(String paymentReference) {
        try {
            Payment payment = razorpayClient.payments.fetch(paymentReference);
            if (!"captured".equals(payment.get("status"))) {
                throw new PaymentGatewayException("Razorpay payment was not captured");
            }
            return BigDecimal.valueOf(((Number) payment.get("amount")).longValue())
                    .divide(BigDecimal.valueOf(100));
        } catch (RazorpayException | ClassCastException ex) {
            throw new PaymentGatewayException("Unable to verify Razorpay payment", ex);
        }
    }
}

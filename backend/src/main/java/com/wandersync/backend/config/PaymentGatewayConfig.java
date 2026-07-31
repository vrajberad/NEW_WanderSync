package com.wandersync.backend.config;

import com.wandersync.backend.service.PaymentGatewayClient;
import com.wandersync.backend.service.RazorpayPaymentGatewayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PaymentGatewayConfig {

    @Bean
    @ConditionalOnProperty(
            prefix = "wandersync.razorpay",
            name = "enabled",
            havingValue = "true")
    public PaymentGatewayClient razorpayPaymentGatewayClient(RazorpayProperties props) {
        if (props.getKeyId() == null || props.getKeyId().isBlank()
                || props.getKeySecret() == null || props.getKeySecret().isBlank()) {
            throw new IllegalStateException(
                    "Razorpay key-id and key-secret are required when Razorpay is enabled");
        }
        return new RazorpayPaymentGatewayClient(props);
    }

    @Bean
    @ConditionalOnMissingBean(PaymentGatewayClient.class)
    public PaymentGatewayClient stubPaymentGatewayClient() {
        return new PaymentGatewayClient() {
            @Override
            public java.math.BigDecimal verifyPayment(String paymentReference) {
                throw unsupported();
            }

            @Override
            public GatewayOrder createOrder(OrderRequest request) {
                throw unsupported();
            }

            @Override
            public boolean verifyWebhookSignature(String payload, String signature) {
                throw unsupported();
            }

            private UnsupportedOperationException unsupported() {
                return new UnsupportedOperationException(
                        "Payment gateway is not enabled; set wandersync.razorpay.enabled=true");
            }
        };
    }
}

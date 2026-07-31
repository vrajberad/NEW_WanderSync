package com.wandersync.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "wandersync.razorpay")
public class RazorpayProperties {

    private boolean enabled;
    private String keyId;
    private String keySecret;
    private String webhookSecret;
    private String currency = "INR";
}

package com.wandersync.backend;

import com.wandersync.backend.config.RazorpayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

// @EnableScheduling powers the expired-seat-lock cleanup job in ScheduledTripService.
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(RazorpayProperties.class)
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}

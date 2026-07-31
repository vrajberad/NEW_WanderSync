package com.wandersync.backend.config;

import com.wandersync.backend.model.ScheduledTrip;
import com.wandersync.backend.model.User;
import com.wandersync.backend.repository.ScheduledTripRepository;
import com.wandersync.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DemoDataSeeder {

    private static final String VENDOR_ONE_EMAIL = "vendor.one@wandersync.local";
    private static final String VENDOR_TWO_EMAIL = "vendor.two@wandersync.local";
    private static final String DEMO_PASSWORD = "WanderSyncDemo!2025";

    private final UserRepository userRepository;
    private final ScheduledTripRepository scheduledTripRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${wandersync.seed.admin-email}")
    private String adminEmail;

    @Value("${wandersync.seed.admin-password}")
    private String adminPassword;

    @Bean
    public org.springframework.boot.CommandLineRunner seedDemoData() {
        return args -> {
            User admin = ensureUser(
                    adminEmail,
                    adminPassword,
                    "WanderSync Administrator",
                    Set.of(User.Role.ROLE_ADMIN)
            );
            User vendorOne = ensureUser(
                    VENDOR_ONE_EMAIL,
                    DEMO_PASSWORD,
                    "Aarav Mehta",
                    Set.of(User.Role.ROLE_VENDOR)
            );
            User vendorTwo = ensureUser(
                    VENDOR_TWO_EMAIL,
                    DEMO_PASSWORD,
                    "Maya Sharma",
                    Set.of(User.Role.ROLE_VENDOR)
            );

            log.info("Dev seed admin available at {}", admin.getEmail());

            if (scheduledTripRepository.count() == 0) {
                scheduledTripRepository.saveAll(List.of(
                        buildTrip(
                                vendorOne.getId(),
                                "Mumbai to Goa Coastal Escape",
                                "Mumbai",
                                "Goa",
                                LocalDate.now().plusDays(14),
                                new BigDecimal("1850.00"),
                                new BigDecimal("1.10")
                        ),
                        buildTrip(
                                vendorTwo.getId(),
                                "Bengaluru to Coorg Coffee Trails",
                                "Bengaluru",
                                "Coorg",
                                LocalDate.now().plusDays(21),
                                new BigDecimal("1250.00"),
                                new BigDecimal("1.05")
                        ),
                        buildTrip(
                                vendorOne.getId(),
                                "Delhi to Jaipur Heritage Weekend",
                                "Delhi",
                                "Jaipur",
                                LocalDate.now().plusDays(28),
                                new BigDecimal("950.00"),
                                new BigDecimal("1.15")
                        )
                ));
                log.info("Seeded demo scheduled trips");
            }
        };
    }

    private User ensureUser(String email, String password, String fullName, Set<User.Role> roles) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .passwordHash(passwordEncoder.encode(password))
                        .fullName(fullName)
                        .roles(roles)
                        .build()));
    }

    private ScheduledTrip buildTrip(String vendorId,
                                    String title,
                                    String origin,
                                    String destination,
                                    LocalDate departureDate,
                                    BigDecimal basePrice,
                                    BigDecimal dynamicYieldMultiplier) {
        return ScheduledTrip.builder()
                .vendorId(vendorId)
                .title(title)
                .origin(origin)
                .destination(destination)
                .departureDate(departureDate)
                .vehicleType("Premium 30-seat coach")
                .basePrice(basePrice)
                .dynamicYieldMultiplier(dynamicYieldMultiplier)
                .seatMap(buildSeatMap(basePrice))
                .availableAddons(List.of(
                        ScheduledTrip.AddOn.builder()
                                .id(UUID.randomUUID().toString())
                                .name("Window seat upgrade")
                                .price(new BigDecimal("150.00"))
                                .build(),
                        ScheduledTrip.AddOn.builder()
                                .id(UUID.randomUUID().toString())
                                .name("Packed regional breakfast")
                                .price(new BigDecimal("220.00"))
                                .build()
                ))
                .build();
    }

    private List<ScheduledTrip.Seat> buildSeatMap(BigDecimal basePrice) {
        List<ScheduledTrip.Seat> seats = new ArrayList<>();
        String[] columns = {"A", "B", "C", "D"};
        BigDecimal aisleSeatSurcharge = new BigDecimal("50.00");

        for (int row = 1; row <= 8; row++) {
            int columnsInRow = row == 8 ? 2 : columns.length;
            for (int column = 0; column < columnsInRow; column++) {
                seats.add(ScheduledTrip.Seat.builder()
                        .seatId(row + columns[column])
                        .row(row)
                        .column(columns[column])
                        .status(ScheduledTrip.SeatStatus.AVAILABLE)
                        .price(basePrice.add(column % 2 == 0 ? BigDecimal.ZERO : aisleSeatSurcharge))
                        .build());
            }
        }
        return seats;
    }
}

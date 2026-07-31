package com.wandersync.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "war_room_lobbies")
public class WarRoomLobby {

    @Id
    private String id;

    private String hostUserId;

    // Optional link to the scheduled trip this lobby is planning around.
    @Indexed
    private String tripId;

    private String tripName;

    private BigDecimal totalCost;

    private BigDecimal sharePerMember;

    private LobbyStatus status; // ACTIVE, ALL_PAID, EXPIRED

    private List<GroupMember> members;

    private List<PollItem> polls;

    private Instant createdAt;

    public enum LobbyStatus {
        ACTIVE,
        ALL_PAID,
        EXPIRED
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GroupMember {
        private String userId;
        private String name;
        private PaymentStatus paymentStatus; // PENDING, PAID
        private BigDecimal paidAmount;
    }

    public enum PaymentStatus {
        PENDING,
        PAID
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PollItem {
        private String id;
        private String title;
        private String category; // DATES, HOTEL, ACTIVITY
        private BigDecimal pricePerPerson;
        private Integer upvotes;
        private Integer downvotes;
    }
}
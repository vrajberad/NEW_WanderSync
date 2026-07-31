package com.wandersync.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    // Never serialized to JSON. Prefer returning UserResponse DTOs anyway.
    @JsonIgnore
    private String passwordHash;

    private String fullName;

    private String phone;

    private Set<Role> roles;

    private Instant createdAt;

    public enum Role {
        ROLE_TRAVELER,
        ROLE_VENDOR,
        ROLE_ADMIN
    }
}

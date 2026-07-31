package com.wandersync.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtUtils {

    // Load from an environment variable (e.g. WANDERSYNC_JWT_SECRET);
    // never commit the secret to source control.
    @Value("${wandersync.jwt.secret}")
    private String jwtSecret;

    @Value("${wandersync.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey signingKey;

    @PostConstruct
    void init() {
        if (jwtSecret == null || jwtSecret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "wandersync.jwt.secret must be at least 256 bits (32 bytes) for HS256");
        }
        signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Parses and verifies the token exactly once. Returns empty when the
     * token is invalid or expired.
     */
    public Optional<Claims> parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public boolean validateToken(String token) {
        return parseToken(token).isPresent();
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).map(Claims::getSubject).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return parseToken(token)
                .map(claims -> (List<String>) claims.get("roles", List.class))
                .orElse(List.of());
    }
}

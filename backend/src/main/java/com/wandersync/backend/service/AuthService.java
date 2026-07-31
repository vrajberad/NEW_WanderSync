package com.wandersync.backend.service;

import com.wandersync.backend.exception.InvalidCredentialsException;
import com.wandersync.backend.exception.ResourceNotFoundException;
import com.wandersync.backend.model.User;
import com.wandersync.backend.repository.UserRepository;
import com.wandersync.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public User registerUser(String email, String password, String fullName, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .fullName(fullName)
                .phone(phone)
                .roles(Set.of(User.Role.ROLE_TRAVELER))
                .createdAt(Instant.now())
                .build();

        return userRepository.save(user);
    }

    public String login(String email, String password) {
        // Same generic error for unknown email and wrong password to
        // prevent user enumeration.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Failed login attempt for unknown email");
                    return new InvalidCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            log.warn("Failed login attempt for user {}", user.getId());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        List<String> roleNames = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        return jwtUtils.generateToken(user.getEmail(), roleNames);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

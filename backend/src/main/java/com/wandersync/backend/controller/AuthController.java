package com.wandersync.backend.controller;

import com.wandersync.backend.dto.AuthResponse;
import com.wandersync.backend.dto.LoginRequest;
import com.wandersync.backend.dto.RegisterRequest;
import com.wandersync.backend.dto.UserResponse;
import com.wandersync.backend.model.User;
import com.wandersync.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User registered = authService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getFullName(),
                request.getPhone()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(registered));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        User user = authService.getUserByEmail(request.getEmail());

        List<String> roleNames = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roleNames)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(UserResponse.from(user));
    }
}

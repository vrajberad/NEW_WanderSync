package com.wandersync.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Roles are intentionally NOT accepted from the client — every new
// account is a ROLE_TRAVELER. Vendor/admin roles must be granted
// through an admin-only endpoint.
@Data
public class RegisterRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 72, message = "Password must be 8-72 characters")
    private String password;

    @NotBlank
    private String fullName;

    private String phone;
}

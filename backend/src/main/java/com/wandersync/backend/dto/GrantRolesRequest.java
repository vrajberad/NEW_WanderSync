package com.wandersync.backend.dto;

import com.wandersync.backend.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrantRolesRequest {

    @NotEmpty(message = "At least one role is required")
    private Set<User.Role> roles;
}

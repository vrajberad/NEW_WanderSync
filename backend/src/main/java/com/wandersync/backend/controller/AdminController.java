package com.wandersync.backend.controller;

import com.wandersync.backend.dto.GrantRolesRequest;
import com.wandersync.backend.dto.UserResponse;
import com.wandersync.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> replaceUserRoles(@PathVariable String id,
                                                         @Valid @RequestBody GrantRolesRequest request) {
        return ResponseEntity.ok(adminService.replaceUserRoles(id, request.getRoles()));
    }
}

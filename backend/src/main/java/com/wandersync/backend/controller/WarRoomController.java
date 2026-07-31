package com.wandersync.backend.controller;

import com.wandersync.backend.dto.CreateLobbyRequest;
import com.wandersync.backend.dto.PayRequest;
import com.wandersync.backend.dto.VoteRequest;
import com.wandersync.backend.model.WarRoomLobby;
import com.wandersync.backend.service.WarRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/war-room")
@RequiredArgsConstructor
public class WarRoomController {

    private final WarRoomService warRoomService;

    @PostMapping("/lobby")
    public ResponseEntity<WarRoomLobby> createLobby(@Valid @RequestBody CreateLobbyRequest request,
                                                    Authentication authentication) {
        WarRoomLobby lobby = warRoomService.createLobby(
                authentication.getName(),
                request.getTripName(),
                request.getTotalCost(),
                request.getMembers()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(lobby);
    }

    @GetMapping("/lobby/{id}")
    public ResponseEntity<WarRoomLobby> getLobby(@PathVariable String id) {
        return ResponseEntity.ok(warRoomService.getLobbyById(id));
    }

    @PostMapping("/lobby/{id}/vote")
    public ResponseEntity<WarRoomLobby> votePollItem(@PathVariable String id,
                                                     @Valid @RequestBody VoteRequest request,
                                                     Authentication authentication) {
        WarRoomLobby lobby = warRoomService.votePollItem(
                id, request.getPollItemId(), request.isUpvote(), authentication.getName());
        return ResponseEntity.ok(lobby);
    }

    @PostMapping("/lobby/{id}/pay")
    public ResponseEntity<WarRoomLobby> processPayment(@PathVariable String id,
                                                       @Valid @RequestBody PayRequest request,
                                                       Authentication authentication) {
        WarRoomLobby lobby = warRoomService.processMemberPayment(
                id, authentication.getName(), request.getPaymentReference());
        return ResponseEntity.ok(lobby);
    }
}

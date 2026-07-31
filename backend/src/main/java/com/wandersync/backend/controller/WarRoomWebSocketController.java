package com.wandersync.backend.controller;

import com.wandersync.backend.dto.PayRequest;
import com.wandersync.backend.dto.VoteRequest;
import com.wandersync.backend.model.WarRoomLobby;
import com.wandersync.backend.service.WarRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

// The Principal comes from JwtChannelInterceptor, which authenticated
// the STOMP CONNECT frame — the acting user is never taken from the payload.
@Controller
@RequiredArgsConstructor
public class WarRoomWebSocketController {

    private final WarRoomService warRoomService;

    @MessageMapping("/war-room/{lobbyId}/vote")
    @SendTo("/topic/war-room/{lobbyId}")
    public WarRoomLobby handleVote(@DestinationVariable String lobbyId,
                                   @Valid VoteRequest voteRequest,
                                   Principal principal) {
        return warRoomService.votePollItem(
                lobbyId, voteRequest.getPollItemId(), voteRequest.isUpvote(), principal.getName());
    }

    @MessageMapping("/war-room/{lobbyId}/pay")
    @SendTo("/topic/war-room/{lobbyId}")
    public WarRoomLobby handlePay(@DestinationVariable String lobbyId,
                                  @Valid PayRequest payRequest,
                                  Principal principal) {
        return warRoomService.processMemberPayment(
                lobbyId, principal.getName(), payRequest.getPaymentReference());
    }
}

package com.wandersync.backend.service;

import com.wandersync.backend.dto.CreateLobbyRequest;
import com.wandersync.backend.exception.ResourceNotFoundException;
import com.wandersync.backend.model.User;
import com.wandersync.backend.model.WarRoomLobby;
import com.wandersync.backend.repository.UserRepository;
import com.wandersync.backend.repository.WarRoomLobbyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

/**
 * NOTE: your original WarRoomService wasn't shared, so this is a full
 * reference implementation matching the fixed controllers. The acting
 * user is always identified by the email from the JWT principal, never
 * by a client-supplied userId.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WarRoomService {

    private final WarRoomLobbyRepository lobbyRepository;
    private final UserRepository userRepository;
    private final PaymentGatewayClient paymentGatewayClient;

    public WarRoomLobby createLobby(String hostEmail, String tripName, BigDecimal totalCost,
                                    List<CreateLobbyRequest.MemberDefinition> members) {
        User host = getUserByEmail(hostEmail);

        BigDecimal sharePerMember = totalCost.divide(
                BigDecimal.valueOf(members.size()), 2, RoundingMode.HALF_UP);

        List<WarRoomLobby.GroupMember> groupMembers = members.stream()
                .map(member -> WarRoomLobby.GroupMember.builder()
                        .userId(member.getUserId())
                        .name(member.getName())
                        .paymentStatus(WarRoomLobby.PaymentStatus.PENDING)
                        .paidAmount(BigDecimal.ZERO)
                        .build())
                .toList();

        WarRoomLobby lobby = WarRoomLobby.builder()
                .hostUserId(host.getId())
                .tripName(tripName)
                .totalCost(totalCost)
                .sharePerMember(sharePerMember)
                .status(WarRoomLobby.LobbyStatus.ACTIVE)
                .members(groupMembers)
                .polls(List.of())
                .createdAt(Instant.now())
                .build();

        return lobbyRepository.save(lobby);
    }

    public WarRoomLobby getLobbyById(String id) {
        return lobbyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lobby not found with id: " + id));
    }

    public WarRoomLobby votePollItem(String lobbyId, String pollItemId, boolean upvote, String voterEmail) {
        WarRoomLobby lobby = getLobbyById(lobbyId);
        User voter = getUserByEmail(voterEmail);
        requireMember(lobby, voter.getId());

        WarRoomLobby.PollItem poll = lobby.getPolls().stream()
                .filter(item -> item.getId().equals(pollItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Poll item not found: " + pollItemId));

        if (upvote) {
            poll.setUpvotes(poll.getUpvotes() == null ? 1 : poll.getUpvotes() + 1);
        } else {
            poll.setDownvotes(poll.getDownvotes() == null ? 1 : poll.getDownvotes() + 1);
        }

        return lobbyRepository.save(lobby);
    }

    public WarRoomLobby processMemberPayment(String lobbyId, String payerEmail, String paymentReference) {
        WarRoomLobby lobby = getLobbyById(lobbyId);
        User payer = getUserByEmail(payerEmail);
        WarRoomLobby.GroupMember member = requireMember(lobby, payer.getId());

        if (member.getPaymentStatus() == WarRoomLobby.PaymentStatus.PAID) {
            // Idempotent: a second call with the same reference is a no-op.
            return lobby;
        }

        // Verify the payment with the gateway server-side; never trust a
        // client-supplied amount.
        BigDecimal verifiedAmount = paymentGatewayClient.verifyPayment(paymentReference);
        if (verifiedAmount.compareTo(lobby.getSharePerMember()) < 0) {
            throw new IllegalStateException("Payment amount does not cover the member share");
        }

        member.setPaymentStatus(WarRoomLobby.PaymentStatus.PAID);
        member.setPaidAmount(verifiedAmount);

        boolean allPaid = lobby.getMembers().stream()
                .allMatch(m -> m.getPaymentStatus() == WarRoomLobby.PaymentStatus.PAID);
        if (allPaid) {
            lobby.setStatus(WarRoomLobby.LobbyStatus.ALL_PAID);
        }

        return lobbyRepository.save(lobby);
    }

    private WarRoomLobby.GroupMember requireMember(WarRoomLobby lobby, String userId) {
        return lobby.getMembers().stream()
                .filter(member -> member.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User is not a member of this lobby"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

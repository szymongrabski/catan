package com.example.backend.gameDetails.invitation;

import com.example.backend.gameDetails.game.GameService;
import com.example.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitation")
public class InvitationController {
    @Autowired
    private InvitationService invitationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<List<InvitationDTO>> getAllInvitations() {
        List<InvitationDTO> invitations = invitationService.getAllInvitationsForCurrentUser();
        return ResponseEntity.ok(invitations);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendInvitation(@RequestParam Long gameId, @RequestParam Long friendId) {
        try {
            invitationService.sendInvitation(gameId, friendId);
            return ResponseEntity.ok("Invitation sent successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/respond")
    public ResponseEntity<Long> respondToInvitation(@RequestParam Long invitationId,
                                                    @RequestParam boolean accept) {
        Long gameId = invitationService.respondToInvitation(invitationId, accept);
        if (gameId != null) {
            return ResponseEntity.ok(gameId);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

package com.example.backend.gameDetails.invitation;

import com.example.backend.gameDetails.game.Game;
import com.example.backend.gameDetails.game.GameService;
import com.example.backend.gameDetails.player.PlayerRole;
import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import com.example.backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.websocket.NotificationsWebSocketHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationsWebSocketHandler webSocketHandler;


    private InvitationDTO convertToDto(Invitation invitation) {
        InvitationDTO dto = new InvitationDTO();
        dto.setId(invitation.getId());
        dto.setAccepted(invitation.getAccepted());
        dto.setSender(userService.convertToUserDTO(invitation.getSender()));
        dto.setReceiver(userService.convertToUserDTO(invitation.getReceiver()));
        return dto;
    }

    public void sendInvitation(Long gameId, Long friendId) {
        Game game = gameService.getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));

        User sender = userService.getCurrentUser();

        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid friend ID"));


        Invitation invitation = new Invitation(game.getId(), sender, friend);
        invitationRepository.save(invitation);

        webSocketHandler.notifyUserAboutGameInvitation(friendId);
    }

    public List<InvitationDTO> getAllInvitationsForCurrentUser() {
        User user = userService.getCurrentUser();

        List<Invitation> invitations = invitationRepository.findByReceiver(user);

        return invitations.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Long respondToInvitation(Long invitationId, boolean accept) {
        User currentUser = userService.getCurrentUser();
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (!invitation.getReceiver().equals(currentUser)) {
            throw new IllegalArgumentException("You are not the recipient of this invitation");
        }

        if (accept) {
            invitation.setAccepted(true);
            invitationRepository.save(invitation);

            gameService.addPlayerToGame(invitation.getGameId(), currentUser, PlayerRole.NORMAL);

            return invitation.getGameId();
        } else {
            invitationRepository.delete(invitation);
            return null;
        }
    }

}

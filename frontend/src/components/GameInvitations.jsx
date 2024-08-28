import { useEffect, useState } from "react";
import {fetchData, respondToGameInvitation} from "../api/authenticatedApi.js";
import {useNavigate} from "react-router-dom";
import {useUser} from "../context/UserContext.jsx";

const GameInvitations = () => {
    const navigate = useNavigate();
    const [isHovered, setHovered] = useState(false);
    const { gameInvitations } = useUser()

    async function handleClick(invitationId, accept) {
        const response = await respondToGameInvitation(invitationId, accept);
        if (response.status === 200) {
           navigate(`/menu/${response.data}`)
        }
    }

    return (
        <>
            {gameInvitations.length > 0 && (
                <div
                    onMouseEnter={() => setHovered(true)}
                    onMouseLeave={() => setHovered(false)}
                >
                    <div className="friend-requests">
                        <h3>Game Invitations</h3>
                        <div className="count-circle">{gameInvitations.length}</div>
                    </div>
                    {isHovered && (
                        <ul>
                            {gameInvitations.map((invitation) => (
                                <li key={invitation.id} className="list-element">
                                    <div>
                                        <div>{invitation.sender.username}</div>
                                        <div>
                                            <button onClick={() => handleClick(invitation.id, true)}>Accept</button>
                                        </div>
                                        <div>
                                            <button onClick={() => handleClick(invitation.id, false)}>Decline</button>
                                        </div>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}
        </>
    );
};

export default GameInvitations;

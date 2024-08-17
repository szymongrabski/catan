import { useEffect, useState } from "react";
import {fetchData, respondToGameInvitation} from "../api/authenticatedApi.js";
import {useNavigate} from "react-router-dom";

const GameInvitations = () => {
    const navigate = useNavigate();
    const [isHovered, setHovered] = useState(false);
    const [gameInvitations, setGameInvitations] = useState([]);

    async function handleClick(invitationId, accept) {
        const response = await respondToGameInvitation(invitationId, accept);
        if (response.status === 200) {
           navigate(`/menu/${response.data}`)
        }
    }

    useEffect(() => {
        const fetchInvitations = async () => {
            try {
                const response = await fetchData('invitation');
                setGameInvitations(response);
            } catch (error) {
                console.error("Failed to fetch invitations:", error);
            }
        };

        fetchInvitations();
    }, []);

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

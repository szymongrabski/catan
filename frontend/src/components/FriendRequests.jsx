import { useState } from "react";
import { respondToInvitation } from "../api/authenticatedApi.js";
import { useUser } from "../context/UserContext.jsx";

const FriendsRequests = () => {
    const { friendRequests, fetchFriends, fetchFriendRequests } = useUser();
    const [isHovered, setHovered] = useState(false);

    async function handleClick(requesterId, accept) {
        await respondToInvitation(requesterId, accept);
        fetchFriendRequests();
        fetchFriends();
    }

    return (
        <>
            {friendRequests.length > 0 && (
                <div
                    onMouseEnter={() => setHovered(true)}
                    onMouseLeave={() => setHovered(false)}
                >
                    <div className="friend-requests">
                        <h3>Friend requests</h3>
                        <div className="count-circle">{friendRequests.length}</div>
                    </div>
                    {isHovered && (
                        <ul>
                            {
                                friendRequests.map((friend) => (
                                <li key={friend.id} className="list-element">
                                    <div>{friend.username}</div>
                                    <div>
                                        <button onClick={() => handleClick(friend.id, true)}>Accept</button>
                                    </div>
                                    <div>
                                        <button onClick={() => handleClick(friend.id, false)}>Decline</button>
                                    </div>
                                </li>
                                ))
                            }
                        </ul>
                    )}
                </div>
            )}
        </>
    );
};

export default FriendsRequests;

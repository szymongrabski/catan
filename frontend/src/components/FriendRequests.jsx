import { useEffect, useState } from "react";
import {fetchData, respondToInvitation} from "../api/authenticatedApi.js";

const FriendsRequests = () => {
    const [friendRequests, setFriendsRequests] = useState([]);
    const [isHovered, setHovered] = useState(false);

    const fetchFriendRequests = async () => {
        try {
            const response = await fetchData('friends/requests');
            setFriendsRequests(response);
        } catch (err) {
            console.error('Error fetching friends:', err);
        }
    };

    async function handleClick(requesterId, accept) {
        await respondToInvitation(requesterId, accept);
        fetchFriendRequests();
    }
    useEffect(() => {
        fetchFriendRequests();
    }, []);

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
                                    <p>{friend.username}</p>
                                    <button onClick={() => handleClick(friend.id, true)}>Accept</button>
                                    <button onClick={() => handleClick(friend.id, false)}>Decline</button>
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

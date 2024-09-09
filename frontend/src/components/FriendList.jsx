import { useState } from "react";
import { useUser } from "../context/UserContext.jsx";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";
import {deleteFriendship, sendGameInvitation} from "../api/authenticatedApi.js";

const FriendsList = ({unfriend=false, invite=false, gameId}) => {
    const { friends, fetchFriends } = useUser();
    const [activeFriendId, setActiveFriendId] = useState(null);

    const toggleMenu = (friendId) => {
        setActiveFriendId(activeFriendId === friendId ? null : friendId);
    };

    const handleUnfriend = async (friendId) => {
        await deleteFriendship(friendId);
        fetchFriends();
    };

    const handleInvite = async (friendId) => {
        await sendGameInvitation(gameId, friendId)
    }

    return (
        <div>
            <h3 className="title">Friends</h3>
            <ul>
                {friends.length === 0 ? (
                    <p>No friends found.</p>
                ) : (
                    friends.map((friend) => (
                        <li key={friend.id} className="list-element">
                            <p>{friend.username}</p>
                            <FontAwesomeIcon icon={faBars} onClick={() => toggleMenu(friend.id)}/>
                            {activeFriendId === friend.id && (
                                <div className="dropdown-menu">
                                    {unfriend && (
                                        <button onClick={() => handleUnfriend(friend.id)}>Unfriend</button>
                                    )}
                                    {invite && (
                                        <button onClick={() => handleInvite(friend.id)}>Invite</button>
                                    )}
                                </div>
                            )}
                        </li>
                    ))
                )}
            </ul>
        </div>
    );
};

export default FriendsList;

import { useEffect, useState } from "react";
import { fetchData } from "../api/authenticatedApi.js";

const FriendsList = () => {
    const [friends, setFriends] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchFriends = async () => {
            try {
                const response = await fetchData('friends');
                setFriends(response);
            } catch (err) {
                setError('Failed to fetch friends');
                console.error('Error fetching friends:', err);
            }
        };

        fetchFriends();
    }, []);

    return (
        <div>
            <h3>Friends</h3>
            {error && <p>{error}</p>}
            <ul>
                {friends.length === 0 ? (
                    <p>No friends found.</p>
                ) : (
                    friends.map((friend) => (
                        <li key={friend.id}>
                            <p>{friend.username}</p>
                        </li>
                    ))
                )}
            </ul>
        </div>
    );
};

export default FriendsList;

import {createContext, useState, useContext, useEffect} from 'react';
import {fetchData} from "../api/authenticatedApi.js";

const UserContext = createContext();

export const UserProvider = ({ children }) => {
    const [friends, setFriends] = useState([]);
    const [friendRequests, setFriendRequests] = useState([]);
    const [gameInvitations, setGameInvitations] = useState([]);
    const [userId, setUserId] = useState(null);
    const [socket, setSocket] = useState(null);

    useEffect(() => {
        fetchUser();
        fetchFriends();
        fetchFriendRequests();
        fetchGameInvitations();

        if (userId) {
            const ws = new WebSocket(`ws://localhost:8080/ws/notifications?user-id=${userId}`);

            ws.onopen = () => {
                console.log("WebSocket connection opened");
            };

            ws.onmessage = (event) => {
                if (event.data === 'friends-request') {
                    fetchFriendRequests();
                } else if (event.data === 'friends-fetch') {
                    fetchFriends();
                } else if (event.data === 'game-invitations') {
                    fetchGameInvitations();
                }
            };

            ws.onclose = () => {
                console.log("WebSocket connection closed");
            };

            setSocket(ws);

            return () => {
                ws.close();
            };
        }
    }, [userId]);

    const fetchGameInvitations = async () => {
        try {
            const response = await fetchData('invitation');
            setGameInvitations(response);
        } catch (error) {
            console.error("Failed to fetch invitations:", error);
        }
    }

    const fetchUser = async() => {
        try {
            const response = await fetchData('user')
            setUserId(response.id)
        } catch (error) {
            console.error("Failed to fetch user");
        }
    }

    const fetchFriendRequests = async () => {
        try {
            const response = await fetchData('friends/requests');
            setFriendRequests(response);
        } catch (err) {
            console.error('Error fetching friends:', err);
        }
    };

    const fetchFriends = async () => {
        try {
            const response = await fetchData('friends');
            setFriends(response);
        } catch (err) {
            console.error('Error fetching friends:', err);
        }
    };

    return (
        <UserContext.Provider value={{ friends, friendRequests, fetchFriends, fetchFriendRequests, userId, gameInvitations}}>
            { children }
        </UserContext.Provider>
    );
};

export const useUser = () => useContext(UserContext);

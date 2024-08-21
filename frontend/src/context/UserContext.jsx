import {createContext, useState, useContext, useEffect} from 'react';
import {fetchData} from "../api/authenticatedApi.js";

const UserContext = createContext();

export const UserProvider = ({ children }) => {
    const [friends, setFriends] = useState([]);
    const [friendRequests, setFriendRequests] = useState([]);
    const [userId, setUserId] = useState(null);
    const [socket, setSocket] = useState(null);

    useEffect(() => {
        fetchUser();
        fetchFriends();
        fetchFriendRequests();

        if (userId) {
            const ws = new WebSocket(`ws://localhost:8080/ws/friend-request?user-id=${userId}`);

            ws.onopen = () => {
                console.log("WebSocket connection opened");
            };

            ws.onmessage = (event) => {
                if (event.data === 'friend-request') {
                    fetchFriendRequests();
                } else if (event.data === 'friend-fetch') {
                    fetchFriends();
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
        <UserContext.Provider value={{ friends, friendRequests, fetchFriends, fetchFriendRequests, userId }}>
            { children }
        </UserContext.Provider>
    );
};

export const useUser = () => useContext(UserContext);

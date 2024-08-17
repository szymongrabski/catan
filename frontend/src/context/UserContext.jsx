import {createContext, useState, useContext, useEffect} from 'react';
import {fetchData} from "../api/authenticatedApi.js";

const UserContext = createContext();

export const UserProvider = ({ children }) => {
    const [friends, setFriends] = useState([]);
    const [friendRequests, setFriendRequests] = useState([]);

    useEffect(() => {
        fetchFriends();
        fetchFriendRequests();
    }, []);

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
        <UserContext.Provider value={{ friends, friendRequests, fetchFriends, fetchFriendRequests }}>
            { children }
        </UserContext.Provider>
    );
};

export const useUser = () => useContext(UserContext);

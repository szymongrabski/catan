import { useState, useEffect } from 'react';
import fetchData from "../api/authenticatedApi.js";

const UserSearch = () => {
    const [username, setUsername] = useState('');
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const [currentUserId, setCurrentUserId] = useState(null);

    useEffect(() => {
        const userId = sessionStorage.getItem('userId');
        if (userId) {
            setCurrentUserId(Number(userId)); // Upewnij się, że userId jest liczbą
        } else {
            console.error('No userId found in sessionStorage');
        }
    }, []);

    const handleSearch = async (event) => {
        event.preventDefault();
        setError(null);

        if (currentUserId === null) {
            setError('User ID not found');
            return;
        }

        try {
            const endpoint = `user/search?username=${encodeURIComponent(username)}&currentUserId=${currentUserId}`;
            const userData = await fetchData(endpoint);
            setUsers(userData);
        } catch (err) {
            setError('Error fetching users');
            console.error('Error:', err);
        }
    };

    return (
        <div>
            <h3>Find friends</h3>
            <form onSubmit={handleSearch}>
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Enter username"
                />
                <button type="submit">Search</button>
            </form>

            <h2>Search Results:</h2>
            {error && <p>{error}</p>}
            <ul>
                {users.map((user) => (
                    <li key={user.userid}>
                        <div>
                            <p>{user.username}</p>
                            <div>
                                {user.friendship_status === 'NONE' && (
                                    <button>
                                        Send request
                                    </button>
                                )}
                                {user.friendship_status === 'PENDING' && (
                                    <button>
                                        Cancel request
                                    </button>
                                )}
                                {user.friendship_status === 'ACCEPTED' && (
                                    <button disabled>
                                        Friends
                                    </button>
                                )}
                            </div>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default UserSearch;

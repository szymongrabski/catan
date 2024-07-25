import { useState } from 'react';
import { fetchUsers, sendInvitation } from "../api/authenticatedApi.js";

const UserSearch = () => {
    const [username, setUsername] = useState('');
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);

    const handleSearch = async (event) => {
        event.preventDefault();
        setError(null);

        try {
            const userData = await fetchUsers(username)
            setUsers(userData);
        } catch (err) {
            setError('Error fetching users');
        }
    };

    const handleInvite = async (userId) => {
        await sendInvitation(userId);
        try {
            const userData = await fetchUsers(username)
            setUsers(userData);
        } catch (err) {
            setError('Error fetching users');
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

            <h5>Search Results:</h5>
            {error && <p>{error}</p>}
            <ul>
                {users.map((user) => (
                    <li key={user.userid}>
                        <div>
                            <p>{user.username}</p>
                            <div>
                                {user.friendship_status === 'NONE' && (
                                    <button onClick={() => handleInvite(user.userid)}>
                                        Send request
                                    </button>
                                )}
                                {user.friendship_status === 'PENDING' && (
                                    <button disabled>
                                        Pending
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

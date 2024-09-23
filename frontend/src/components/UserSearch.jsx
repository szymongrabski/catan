import { useState } from 'react';
import { fetchUsers, sendInvitation } from "../api/authenticatedApi.js";

const UserSearch = () => {
    const [username, setUsername] = useState('');
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);

    const handleSearch = async (event) => {
        event.preventDefault();
        setError(null);

        if (username.trim().length < 4) {
            setError('Username must be at least 4 characters long');
            setUsers([]);
            return;
        }

        try {
            const userData = await fetchUsers(username);
            setUsers(userData);
        } catch (err) {
            setError('Error fetching users');
        }
    };

    const handleInvite = async (userId) => {
        await sendInvitation(userId);
        try {
            const userData = await fetchUsers(username);
            setUsers(userData);
        } catch (err) {
            setError('Error fetching users');
        }
    };

    const handleClear = () => {
        setUsername('');
        setUsers([]);
        setError(null);
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
                    className="input"
                />
                <button type="submit">Search</button>
                {error || users.length > 0 ? (
                    <button type="button" onClick={handleClear}>
                        Clear
                    </button>
                ) : null}
            </form>
            {error && <p className="error">{error}</p>}
            <ul>
                {users.map((user) => (
                    <li key={user.userid} className="list-element">
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
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default UserSearch;

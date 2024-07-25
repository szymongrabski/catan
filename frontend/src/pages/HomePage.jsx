import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import UserSearch from "../components/UserSearch.jsx";
import FriendList from "../components/FriendList.jsx";

function HomePage() {
    const navigate = useNavigate();
    const authToken = sessionStorage.getItem('authToken');

    useEffect(() => {

        if (!authToken) {
            navigate('/login');
        }
    }, [authToken]);

    return (
        <div className="test-container">
            <h1>Home</h1>
            <div className="test">
                <FriendList />
                <UserSearch />
            </div>
        </div>
    );
}

export default HomePage;

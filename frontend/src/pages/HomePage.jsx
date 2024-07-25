import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import UserSearch from "../components/UserSearch.jsx";
import FriendList from "../components/FriendList.jsx";
import FriendRequests from "../components/FriendRequests.jsx";

function HomePage() {
    const navigate = useNavigate();
    const authToken = sessionStorage.getItem('authToken');

    useEffect(() => {

        if (!authToken) {
            navigate('/login');
        }
    }, [authToken]);

    return (
        <div className="page">
            <div className="home-page">
                <div>
                    <FriendRequests/>
                    <FriendList />
                </div>
                <div className="content">
                    <div className="circle">
                    </div>
                    <button>Start game</button>
                </div>
                <UserSearch/>
            </div>
        </div>
    );
}

export default HomePage;

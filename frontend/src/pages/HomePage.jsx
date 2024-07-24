import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import UserSearch from "../components/UserSearch.jsx";

function HomePage() {
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);
    const authToken = sessionStorage.getItem('authToken');

    useEffect(() => {

        if (!authToken) {
            navigate('/login');
        }
    }, [authToken]);

    return (
        <>
            <h1>Home</h1>
            <div>
                <UserSearch />
            </div>
        </>
    );
}

export default HomePage;

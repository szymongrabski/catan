import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

function HomePage() {
    const navigate = useNavigate();
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
                <p>Find friends</p>
            </div>
        </>
    );
}

export default HomePage;

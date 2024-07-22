import { useNavigate } from "react-router-dom";
import LoginForm from "../components/LoginForm.jsx";

function LoginPage() {
    const navigate = useNavigate();

    return (
        <>
            <h1>Login</h1>
            <LoginForm/>
            <div>
                <p>You don't have an account?</p>
                <button onClick={() => navigate("/register")}>Register</button>
            </div>
        </>
    );
}

export default LoginPage;

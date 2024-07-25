import { useNavigate } from "react-router-dom";
import LoginForm from "../components/LoginForm.jsx";
import logo from '../assets/logo.svg';

function LoginPage() {
    const navigate = useNavigate();

    return (
        <div className="page">
            <div className="form-page">
                <div>
                    <img src={logo} alt="logo" className="logo"/>
                </div>
                <div>
                    <h2 className="form-title">Login</h2>
                    <LoginForm/>
                </div>
                <div>
                    <p>You don't have an account?</p>
                    <button onClick={() => navigate("/register")}>Register</button>
                </div>
            </div>
        </div>
    );
}

export default LoginPage;

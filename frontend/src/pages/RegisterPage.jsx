import {useNavigate} from "react-router-dom";
import RegisterForm from "../components/RegisterForm.jsx";
import logo from "../assets/logo.svg";

function RegisterPage() {
    const navigate = useNavigate();
    return (
        <div className="page">
            <div className="form-page">
                <div>
                    <img src={logo} alt="logo" className="logo"/>
                </div>
                <div>
                    <h2 className="form-title">Register</h2>
                    <RegisterForm/>
                </div>
                <div>
                    <p>Already have an account?</p>
                    <button onClick={() => navigate("/login")}>Login</button>
                </div>
            </div>
        </div>
    )
}

export default RegisterPage

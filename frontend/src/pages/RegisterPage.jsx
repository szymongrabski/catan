import {useNavigate} from "react-router-dom";
import RegisterForm from "../components/RegisterForm.jsx";

function RegisterPage() {
    const navigate = useNavigate();
    return (
        <>
            <h1>Register</h1>
            <RegisterForm />
            <div>
                <p>Already have an account?</p>
                <button onClick={() => navigate("/login")}>Login</button>
            </div>
        </>
    )
}

export default RegisterPage

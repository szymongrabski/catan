import {useNavigate} from "react-router-dom";

function RegisterPage() {
    const navigate = useNavigate();
    return (
        <>
            <h1>Register</h1>
            <div>
                <p>Already have an account?</p>
                <button onClick={() => navigate("/login")}>Login</button>
            </div>
        </>
    )
}

export default RegisterPage

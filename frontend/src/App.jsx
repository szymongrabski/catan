import './assets/styles/main.scss';
import {
    BrowserRouter as Router, Navigate, Route, Routes
} from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import HomePage from "./pages/HomePage.jsx";

function App() {

  return (
      <Router>
        <Routes>
            <Route path="/" element={<Navigate to="login"/>} />
            <Route path="/login" element={<LoginPage/>} />
            <Route path="/register" element={<RegisterPage/>} />
            <Route path="/home" element={<HomePage/>} />
        </Routes>
      </Router>
  )
}

export default App

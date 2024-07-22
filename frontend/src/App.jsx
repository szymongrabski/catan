import './App.css'
import {
    BrowserRouter as Router, Navigate, Route, Routes
} from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";

function App() {

  return (
      <Router>
        <Routes>
            <Route path="/" element={<Navigate to="login"/>} />
            <Route path="/login" element={<LoginPage/>} />
            <Route path="/register" element={<RegisterPage/>} />
        </Routes>
      </Router>
  )
}

export default App

import './assets/styles/main.scss';
import {
    BrowserRouter as Router, Navigate, Route, Routes
} from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import HomePage from "./pages/HomePage.jsx";
import {UserProvider} from "./context/UserContext.jsx";
import MenuPage from "./pages/MenuPage.jsx";
import {GameProvider} from "./context/GameContext.jsx";
import GamePage from "./pages/GamePage.jsx";

function App() {

  return (
      <Router>
        <Routes>
            <Route path="/" element={<Navigate to="login"/>} />
            <Route path="/login" element={<LoginPage/>} />
            <Route path="/register" element={<RegisterPage/>} />
            <Route path="/home" element={<UserProvider><HomePage/></UserProvider>} />
            <Route path="/menu/:gameId" element={<UserProvider><GameProvider><MenuPage/></GameProvider></UserProvider>} />
            <Route  path="/:gameId/game" element={<UserProvider><GameProvider><GamePage/></GameProvider></UserProvider>} />
        </Routes>
      </Router>
  )
}

export default App

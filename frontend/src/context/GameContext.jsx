import {createContext, useState, useContext, useEffect} from 'react';
import { fetchData } from "../api/authenticatedApi.js";

const GameContext = createContext();

export const GameProvider = ({ children }) => {
    return (
        <GameContext.Provider value={{ }}>
            { children }
        </GameContext.Provider>
    );
};

export const useGame = () => useContext(GameContext);

import { useGame } from "../context/GameContext.jsx";
import {useEffect} from "react";

const typeColors = {
    WOOD: '#295727',
    BRICK: '#9e3923',
    WOOL: '#74c622',
    WHEAT: '#EEB902',
    ROCK: '#97968f',
    default: '#baa380'
};

const ResourcePanel = () => {
    const { player, players } = useGame();
    const currentPlayer = players.find(p => p.id == player.id);


    if (currentPlayer) {
        return (
            <div className="resource-panel">
                <div style={{ backgroundColor: typeColors.WOOD }}>
                    <p>Wood</p>
                    <p>{currentPlayer.resources.WOOD}</p>
                </div>
                <div style={{ backgroundColor: typeColors.BRICK }}>
                    <p>Brick</p>
                    <p>{currentPlayer.resources.BRICK}</p>
                </div>
                <div style={{ backgroundColor: typeColors.WOOL }}>
                    <p>Wool</p>
                    <p>{currentPlayer.resources.WOOL}</p>
                </div>
                <div style={{ backgroundColor: typeColors.WHEAT }}>
                    <p>Wheat</p>
                    <p>{currentPlayer.resources.WHEAT}</p>
                </div>
                <div style={{ backgroundColor: typeColors.ROCK }}>
                    <p>Rock</p>
                    <p>{currentPlayer.resources.ROCK}</p>
                </div>
            </div>
        );
    }

    return null;
};

export default ResourcePanel;

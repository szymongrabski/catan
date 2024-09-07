import { useGame } from "../context/GameContext.jsx";

const typeColors = {
    WOOD: '#295727',
    BRICK: '#9e3923',
    WOOL: '#74c622',
    WHEAT: '#EEB902',
    ROCK: '#97968f',
    default: '#baa380'
};

const ResourcePanel = () => {
    const { player } = useGame();

    if (player) {
        return (
            <div className="resource-panel">
                <div style={{ backgroundColor: typeColors.WOOD }}>
                    <p>Wood</p>
                    <p>{player.resources.WOOD}</p>
                </div>
                <div style={{ backgroundColor: typeColors.BRICK }}>
                    <p>Brick</p>
                    <p>{player.resources.BRICK}</p>
                </div>
                <div style={{ backgroundColor: typeColors.WOOL }}>
                    <p>Wool</p>
                    <p>{player.resources.WOOL}</p>
                </div>
                <div style={{ backgroundColor: typeColors.WHEAT }}>
                    <p>Wheat</p>
                    <p>{player.resources.WHEAT}</p>
                </div>
                <div style={{ backgroundColor: typeColors.ROCK }}>
                    <p>Rock</p>
                    <p>{player.resources.ROCK}</p>
                </div>
            </div>
        );
    }

    return null;
};

export default ResourcePanel;

import {useGame} from "../context/GameContext.jsx";

const ResourcePanel = () => {
    const { player } = useGame()

    if (player) {
        return (
            <div className="resource-panel">
                <div>
                    <p>Wood</p>
                    <p>{player.resources.WOOD}</p>
                </div>
                <div>
                    <p>Brick</p>
                    <p>{player.resources.BRICK}</p>
                </div>
                <div>
                    <p>Wool</p>
                    <p>{player.resources.WOOL}</p>
                </div>
                <div>
                    <p>Wheat</p>
                    <p>{player.resources.WOOL}</p>
                </div>
                <div>
                    <p>Rock</p>
                    <p>{player.resources.ROCK}</p>
                </div>
            </div>
        );
    }
};

export default ResourcePanel;

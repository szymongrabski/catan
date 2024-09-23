import { useGame } from "../context/GameContext.jsx";
import wood from "../assets/wood.svg";
import sheep from "../assets/sheep.svg";
import brick from "../assets/brick.svg";
import wheat from "../assets/wheat.svg";
import rock from "../assets/rock.svg";


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
                <div style={{backgroundColor: typeColors.WOOD}} className="resource-panel-item">
                    <div className="resource-icon-container">
                        <img src={wood} alt="wood" className="resource-icon"/>
                    </div>
                    <p>{currentPlayer.resources.WOOD}</p>
                </div>
                <div style={{backgroundColor: typeColors.BRICK}} className="resource-panel-item">
                    <div className="resource-icon-container">
                        <img src={brick} alt="brick" className="resource-icon"/>
                    </div>
                    <p>{currentPlayer.resources.BRICK}</p>
                </div>
                <div style={{backgroundColor: typeColors.WOOL}} className="resource-panel-item">
                    <div className="resource-icon-container">
                    <img src={sheep} alt="sheep" className="resource-icon"/>
                    </div>
                    <p>{currentPlayer.resources.WOOL}</p>
                </div>
                <div style={{backgroundColor: typeColors.WHEAT}} className="resource-panel-item">
                    <div className="resource-icon-container">
                        <img src={wheat} alt="wheat" className="resource-icon"/>
                    </div>
                    <p>{currentPlayer.resources.WHEAT}</p>
                </div>
                <div style={{backgroundColor: typeColors.ROCK}} className="resource-panel-item">
                    <div className="resource-icon-container">
                        <img src={rock} alt="rock" className="resource-icon"/>
                    </div>
                    <p>{currentPlayer.resources.ROCK}</p>
                </div>
            </div>
        );
    }

    return null;
};

export default ResourcePanel;

import { useEffect, useState } from "react";
import Modal from "react-modal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import wood from "../assets/wood.svg";
import sheep from "../assets/sheep.svg";
import brick from "../assets/brick.svg";
import wheat from "../assets/wheat.svg";
import rock from "../assets/rock.svg";
import {substractResources} from "../api/authenticatedApi.js";
import {useGame} from "../context/GameContext.jsx";


const resourceIcons = {
    wood: wood,
    wool: sheep,
    brick: brick,
    wheat: wheat,
    rock: rock,
};

const SevenModal = ({ isOpen, onRequestClose, player, gameId }) => {
    const [totalResources, setTotalResources] = useState(0);
    const [resourcesToGive, setResourcesToGive] = useState({});
    const total = Object.values(player.resources).reduce((sum, value) => sum + (value || 0), 0);
    const maxToGive = Math.floor(total / 2);

    useEffect(() => {
        const initialResources = Object.keys(player.resources).reduce((acc, resource) => {
            acc[resource] = 0;
            return acc;
        }, {});
        setResourcesToGive(initialResources);
        setTotalResources(0);
    }, [player]);

    const onSubmit = async () => {
        const response = await substractResources(gameId, player.id, resourcesToGive);
    }

    const handleResourceChange = (resource, amount) => {
        const currentAmount = resourcesToGive[resource] || 0;
        const newAmount = currentAmount + amount;


        if (newAmount >= 0 && newAmount <= player.resources[resource]) {
            setResourcesToGive(prev => ({
                ...prev,
                [resource]: newAmount
            }));

            setTotalResources(prev => prev + amount);
        }
    };

    if (total > 7) {
        return (
            <Modal
                isOpen={isOpen}
                onRequestClose={onRequestClose}
                contentLabel="Give away resources"
                className="modal"
                overlayClassName="overlay"
            >
                <h2 className="title">Give away resources</h2>
                <p className="title">You have {total} resources, you need to give away {maxToGive}.</p>
                <ul className="seven-list">
                    {Object.entries(player.resources).map(([resource, value]) => (
                        <li key={resource}>
                            <div className="seven-item">
                                <div>
                                    <p>
                                        {value !== null && value !== undefined && !isNaN(value - (resourcesToGive[resource] || 0))
                                            ? value - (resourcesToGive[resource] || 0)
                                            : 'No resource found.'}
                                    </p>
                                    <div className="resource-icon-container">
                                        <img src={resourceIcons[resource.toLowerCase()]} alt={resource.toLowerCase()}
                                             className="resource-icon"/>
                                    </div>
                                </div>
                                <button
                                    onClick={() => handleResourceChange(resource, -1)}
                                    disabled={resourcesToGive[resource] <= 0}
                                    className="seven-button"
                                >
                                    <FontAwesomeIcon icon={faMinus}/>
                                </button>
                                <p>{resourcesToGive[resource]}</p>
                                <button
                                    onClick={() => handleResourceChange(resource, 1)}
                                    disabled={resourcesToGive[resource] >= player.resources[resource] || totalResources >= maxToGive}
                                    className="seven-button"
                                >
                                    <FontAwesomeIcon icon={faPlus}/>
                                </button>

                            </div>
                        </li>
                    ))}
                </ul>
                <div className="seven-submit-button-container">
                    <button
                        onClick={() => {
                            onSubmit()
                            onRequestClose();
                        }}
                        className="seven-submit-button"
                        disabled={totalResources < maxToGive}
                    >
                        Submit
                    </button>
                </div>
            </Modal>
        );
    }
};

export default SevenModal;

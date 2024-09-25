import React, {useEffect, useState} from 'react';
import Hexagon from "./Hexagon.jsx";
import { useGame } from "../context/GameContext.jsx";
import {fetchData, placeRoad, placeSettlement, upgradeSettlement} from "../api/authenticatedApi.js";
import AvailableVertex from "./AvailableVertex.jsx";
import Settlement from "./Settlement.jsx";
import AvailableRoad from "./AvailableRoad.jsx";
import Road from "./Road.jsx";
import UpgradeVertex from "./UpgradeVertex.jsx";
import SevenModal from "./SevenModal.jsx";

const size = 100;

const colors = ['red', 'blue', 'cyan', 'magenta'];

const Board = () => {
    const { gameId, board, player, currentPlayerIndex, fetchCurrentPlayerIndex, settlements, availableRoads, roads, fetchAvailableRoads, gameRound, players, diceNumber, robberHex, isRobberPlaced } = useGame();
    const [availableVertices, setAvailableVertices] = useState([]);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const openModal = () => setIsModalOpen(true);
    const closeModal = () => {
        setIsModalOpen(false);
    }

    useEffect(() => {
        fetchCurrentPlayerIndex()
        if (currentPlayerIndex === player.id) {
            fetchAvailableVertices();
            fetchAvailableRoads();
        }
    }, [currentPlayerIndex, players]);

    const fetchAvailableVertices = async () => {
        const response = await fetchData(`game/${gameId}/${player.id}/available-vertices`);
        setAvailableVertices(response);
    }

    const countPlayersRoads = () => {
        return roads.filter(road => road.ownerId === player.id).length;
    }

    const countPlayersSettlements = () => {
        return settlements.filter(set => set.ownerId === player.id).length;
    }

    const hasEnoughResourcesToUpgrade = players.filter(p => p.id === player.id && p.resources.WHEAT >= 2 && p.resources.ROCK >= 3).length > 0;

    const calculateVertexPosition = (q, r, direction) => {
        const width = Math.sqrt(3) * size;
        const height = 2 * size;

        const x = width * (q + r / 2);
        const y = (3 / 2) * size * r;

        switch (direction) {
            case 'N':
                return [x, y - size];
            case 'S':
                return [x, y + size];
            default:
                return [x, y];
        }
    };

    const onAvailableVertexClick = async (q, r, direction) => {
        try {
            await placeSettlement(gameId, player.id, q, r, direction);
            fetchAvailableVertices();
        } catch (error) {
            console.error(error);
        }
    }

    const onUpgradeVertexClick = async (q, r, direction) => {
        try {
            console.log("klik")
            await upgradeSettlement(gameId, player.id, q, r, direction);
        } catch (error) {
            console.error(error);
        }
    }

    const onRoadClick = async (road) => {
        try {
            await placeRoad(gameId, player.id, road);
            fetchAvailableRoads();
        } catch (error) {
            console.error("Error placing road:", error);
        }
    };


    if (board) {
        return (
            <svg width="80%" height="80%" viewBox="-580 -200 1500 1000">
                {board.hexes.map((hex, hexIndex) => {
                    const isRobber = hex.q === robberHex.q && hex.r === robberHex.r;
                    return (
                        <Hexagon
                            key={`${hex.q}-${hex.r}`}
                            q={hex.q}
                            r={hex.r}
                            size={size}
                            type={hex.type}
                            number={hex.number}
                            isRobber={isRobber}
                        />
                    )})}

                {diceNumber === 7 && (
                    <SevenModal
                        isOpen={true}
                        onRequestClose={closeModal}
                        player={players.filter(p => p.id === player.id)[0]}
                        gameId={gameId}
                    />
                )}

                {diceNumber === 7 && !isModalOpen && (
                    <div className="modal">
                        test
                    </div>
                )}


                {currentPlayerIndex == player.id && gameRound <= 1 && countPlayersSettlements() < gameRound + 1 && availableVertices.map((vertex, vertexIndex) => {
                    const [vx, vy] = calculateVertexPosition(vertex.q, vertex.r, vertex.direction);

                    return (
                        <AvailableVertex
                            key={vertexIndex}
                            x={vx}
                            y={vy}
                            q = {vertex.q}
                            r = {vertex.r}
                            direction = {vertex.direction}
                            onClick={onAvailableVertexClick}
                        />
                    );
                })}

                {currentPlayerIndex == player.id && gameRound > 1 && diceNumber !== 0 && isRobberPlaced && availableVertices.map((vertex, vertexIndex) => {
                    const [vx, vy] = calculateVertexPosition(vertex.q, vertex.r, vertex.direction);

                    return (
                        <AvailableVertex
                            key={vertexIndex}
                            x={vx}
                            y={vy}
                            q = {vertex.q}
                            r = {vertex.r}
                            direction = {vertex.direction}
                            onClick={onAvailableVertexClick}
                        />
                    );
                })}


                {currentPlayerIndex == player.id && gameRound <= 1 && countPlayersRoads() < gameRound + 1 && countPlayersSettlements() > countPlayersRoads() && availableRoads.map((road, roadIndex) => (
                    <AvailableRoad
                        key={roadIndex}
                        road={road}
                        calculateVertexPosition={calculateVertexPosition}
                        onRoadClick={onRoadClick}
                    />
                ))}


                {currentPlayerIndex == player.id && gameRound > 1 && diceNumber !== 0 && isRobberPlaced && availableRoads.map((road, roadIndex) => (
                    <AvailableRoad
                        key={roadIndex}
                        road={road}
                        calculateVertexPosition={calculateVertexPosition}
                        onRoadClick={onRoadClick}
                    />
                ))}



                { (diceNumber === 0 || (diceNumber !== 0 && !hasEnoughResourcesToUpgrade)) &&
                    settlements.map((vertex, vertexIndex) => {
                        const [vx, vy] = calculateVertexPosition(vertex.q, vertex.r, vertex.direction);
                        const settlementColor = colors[vertex.ownerId % colors.length];
                        return (
                            <Settlement
                                key={vertexIndex}
                                x={vx}
                                y={vy}
                                color={settlementColor}
                                upgraded={vertex.upgraded}
                            />
                        );
                    })
                }

                { diceNumber !== 0 && hasEnoughResourcesToUpgrade &&
                    settlements.map((vertex, vertexIndex) => {
                        const [vx, vy] = calculateVertexPosition(vertex.q, vertex.r, vertex.direction);
                        const settlementColor = colors[vertex.ownerId % colors.length];
                        if (!vertex.upgraded && vertex.ownerId === player.id && isRobberPlaced && currentPlayerIndex === player.id) {
                            return (
                                <UpgradeVertex
                                    key={vertexIndex}
                                    x={vx}
                                    y={vy}
                                    q = {vertex.q}
                                    r = {vertex.r}
                                    direction = {vertex.direction}
                                    onClick={onUpgradeVertexClick}
                                />
                            )
                        } else {
                            return (
                                <Settlement
                                    key={vertexIndex}
                                    x={vx}
                                    y={vy}
                                    color={settlementColor}
                                    upgraded={vertex.upgraded}
                                />
                            );
                        }
                })}


                {roads.map((road, roadIndex) => {
                    const settlementColor = colors[ road.ownerId % colors.length];
                    return (
                        <Road
                            key={roadIndex}
                            road={road}
                            calculateVertexPosition={calculateVertexPosition}
                            color={settlementColor}
                        />
                    );
                })}
            </svg>
        );
    }

    return null;
};

export default Board;

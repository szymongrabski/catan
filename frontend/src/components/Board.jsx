import React, {useEffect, useState} from 'react';
import Hexagon from "./Hexagon.jsx";
import { useGame } from "../context/GameContext.jsx";
import {fetchData, placeRoad, placeSettlement} from "../api/authenticatedApi.js";
import AvailableVertex from "./AvailableVertex.jsx";
import Settlement from "./Settlement.jsx";
import AvailableRoad from "./AvailableRoad.jsx";
import Road from "./Road.jsx";

const size = 100;

const Board = () => {
    const { gameId, board, player, currentPlayerIndex, fetchCurrentPlayerIndex, settlements, availableRoads, roads, fetchAvailableRoads, gameRound } = useGame();
    const [availableVertices, setAvailableVertices] = useState([]);


    useEffect(() => {
        fetchCurrentPlayerIndex()
        if (currentPlayerIndex === player.id) {
            fetchAvailableVertices();
            fetchAvailableRoads();
        }
    }, [currentPlayerIndex]);

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
                {board.hexes.map((hex, hexIndex) => (
                    <Hexagon
                        key={`${hex.q}-${hex.r}`}
                        q={hex.q}
                        r={hex.r}
                        size={size}
                        type={hex.type}
                        number={hex.number}
                    />
                ))}

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

                {currentPlayerIndex == player.id && gameRound <= 1 && countPlayersRoads() < gameRound + 1 && countPlayersSettlements() > countPlayersRoads() && availableRoads.map((road, roadIndex) => (
                    <AvailableRoad
                        key={roadIndex}
                        road={road}
                        calculateVertexPosition={calculateVertexPosition}
                        onRoadClick={onRoadClick}
                    />
                ))}

                {settlements.map((vertex, vertexIndex) => {
                    const [vx, vy] = calculateVertexPosition(vertex.q, vertex.r, vertex.direction);

                    return (
                        <Settlement
                            key={vertexIndex}
                            x={vx}
                            y={vy}
                            color={"blue"}
                        />
                    );
                })}

                {roads.map((road, roadIndex) => (
                    <Road
                        key={roadIndex}
                        road={road}
                        calculateVertexPosition={calculateVertexPosition}
                        />
                ))}

            </svg>
        );
    }

    return null;
};

export default Board;

import React, {useEffect, useState} from 'react';
import Hexagon from "./Hexagon.jsx";
import { useGame } from "../context/GameContext.jsx";
import {fetchData, placeSettlement} from "../api/authenticatedApi.js";
import AvailableVertex from "./AvailableVertex.jsx";
import Settlement from "./Settlement.jsx";

const size = 100;

const Board = () => {
    const { gameId, board, isPlayersTurn, player, currentPlayerIndex, fetchCurrentPlayerIndex, settlements } = useGame();
    const [availableVertices, setAvailableVertices] = useState([]);

    useEffect(() => {
        fetchCurrentPlayerIndex()
        if (isPlayersTurn) {
            fetchAvailableVertices();
        }

        console.log(settlements)
    }, []);

    const fetchAvailableVertices = async () => {
        const response = await fetchData(`game/${gameId}/available-vertices`);
        setAvailableVertices(response);
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


    if (board) {
        return (
            <svg width="100%" height="100%" viewBox="-600 -200 1500 1000">
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

                {isPlayersTurn() && availableVertices.map((vertex, vertexIndex) => {
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

                {settlements.length > 0 && settlements.map((vertex, vertexIndex) => {
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
            </svg>
        );
    }

    return null;
};

export default Board;

import React, {useEffect, useState} from 'react';
import Hexagon from "./Hexagon.jsx";
import { useGame } from "../context/GameContext.jsx";
import {fetchData} from "../api/authenticatedApi.js";
import AvailableVertex from "./AvailableVertex.jsx";

const size = 100;

const Board = () => {
    const { gameId, board, isPlayersTurn, player, currentPlayerIndex } = useGame();
    const [availableVertices, setAvailableVertices] = useState([]);

    useEffect(() => {
        console.log(currentPlayerIndex, player.id);
        if (isPlayersTurn()) {
            fetchAvailableVertices();
        }
    }, [isPlayersTurn]);

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

    const onAvailableVertexClick = () => {
        console.log("Available Vertex");
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
            </svg>
        );
    }

    return null;
};

export default Board;

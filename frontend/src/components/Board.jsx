import React, {useEffect} from 'react';
import Hexagon from "./Hexagon.jsx"
import {useGame} from "../context/GameContext.jsx";

const size = 100;

const Board = () => {
    const { board } = useGame()

    useEffect(() => {
        if (board) {
            console.log("iN BOARD" + board);
        }
    }, [board]);
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
                    )
                )}

            </svg>
        );
    }
};

export default Board;


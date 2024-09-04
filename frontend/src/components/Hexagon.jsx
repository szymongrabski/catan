import React from 'react';

const Hexagon = ({ q, r, size, type, number }) => {
    const width = Math.sqrt(3) * size;
    const height = 2 * size;

    const x = width * (q + r / 2);
    const y = (3 / 2) * size * r;

    // Define the hexagon points
    const points = [
        [Math.sqrt(3) / 2 * size, -size / 2],
        [0, -size],
        [-Math.sqrt(3) / 2 * size, -size / 2],
        [-Math.sqrt(3) / 2 * size, size / 2],
        [0, size],
        [Math.sqrt(3) / 2 * size, size / 2]
    ]
        .map(([px, py]) => `${px + x},${py + y}`)
        .join(' ');

    const typeColors = {
        WOOD: '#74c622',
        BRICK: '#9e3923',
        WOOL: '#d1d9c9',
        WHEAT: '#f1dd36',
        ROCK: '#97968f',
        default: 'white'
    };

    const fillColor = typeColors[type] || typeColors.default;

    return (
        <g>
            <polygon points={points} fill={fillColor} stroke="black" />
            <text x={x} y={y + 5} textAnchor="middle" fontSize={size / 2} fill="black">
                {number}
            </text>
        </g>
    );
};

export default Hexagon;

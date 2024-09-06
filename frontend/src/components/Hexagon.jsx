import React from 'react';

const numberMapping = {
    one: 1,
    two: 2,
    three: 3,
    four: 4,
    five: 5,
    six: 6,
    seven: 7,
    eight: 8,
    nine: 9,
    ten: 10,
    eleven: 11,
    twelve: 12,
};

const Hexagon = ({ q, r, size, type, number }) => {
    const width = Math.sqrt(3) * size;
    const height = 2 * size;

    const x = width * (q + r / 2);
    const y = (3 / 2) * size * r;

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
        WOOD: '#295727',
        BRICK: '#9e3923',
        WOOL: '#74c622',
        WHEAT: '#f1dd36',
        ROCK: '#97968f',
        default: '#baa380'
    };

    const fillColor = typeColors[type] || typeColors.default;

    const numberValue = numberMapping[number.toLowerCase()] || null;
    const textColor = (numberValue === 6 || numberValue === 8) ? 'red' : 'black';
    const displayNumber = numberValue ? numberValue : '';


    return (
        <g>
            <polygon points={points} fill={fillColor} stroke="black"/>
            {displayNumber !== '' && (
                <>
                    <circle cx={x} cy={y} r={size / 3} fill="white"/>
                    <text x={x} y={y + 10} textAnchor="middle" fontSize={size / 3} fill={textColor} fontWeight="bold">
                        {displayNumber}
                    </text>
                </>
            )}
        </g>
    );
};

export default Hexagon;

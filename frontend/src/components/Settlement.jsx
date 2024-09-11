import React from 'react';

const Settlement = ({ x, y, color, upgraded }) => {
    return upgraded ? (
        <rect
            x={x - 20}
            y={y - 20}
            width={40}
            height={40}
            fill={color}
            transform={`rotate(45, ${x}, ${y})`}
        />
    ) : (
        <circle cx={x} cy={y} r={20} fill={color} />
    );
};

export default Settlement;

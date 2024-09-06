import React from 'react';

const Settlement = ({ x, y, color }) => {
    return (
        <circle cx={x} cy={y} r={20} fill={color} stroke="black" />
    );
};

export default Settlement;
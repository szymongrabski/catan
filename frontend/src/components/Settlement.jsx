import React from 'react';

const Settlement = ({ x, y, color }) => {
    return (
        <circle cx={x} cy={y} r={5} fill={color} stroke="black" />
    );
};

export default Settlement;
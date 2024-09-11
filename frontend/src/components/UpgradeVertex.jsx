import React from 'react';

const UpgradeVertex = ({ x, y, onClick, q, r, direction }) => {
    return (
        <foreignObject
            x={x - 20}
            y={y - 20}
            width="40"
            height="40"
        >
            <button
                onClick={() => onClick(q, r, direction)}
                style={{
                    width: '40px',
                    height: '40px',
                    borderRadius: '50%',
                    backgroundColor: "white",
                    border: '1px solid black',
                    padding: '0',
                    margin: '0',
                    cursor: 'pointer',
                    outline: 'none',
                    zIndex: 10,
                }}
            />
        </foreignObject>
    );
};

export default UpgradeVertex;

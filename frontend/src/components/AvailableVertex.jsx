import React from 'react';

const AvailableVertex = ({ x, y, onClick, q, r, direction }) => {
    return (
        <foreignObject
            x={x - 10}
            y={y - 10}
            width="20"
            height="20"
        >
            <button
                onClick={() => onClick(q, r, direction)}
                style={{
                    width: '20px',
                    height: '20px',
                    borderRadius: '50%',
                    backgroundColor: "white",
                    border: '1px solid black',
                    padding: '0',
                    margin: '0',
                    cursor: 'pointer',
                    outline: 'none',
                }}
            />
        </foreignObject>
    );
};

export default AvailableVertex;

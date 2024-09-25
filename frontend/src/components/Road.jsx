import React from 'react';

const Road = ({ road, calculateVertexPosition, color }) => {
    const [midX, midY] = calculateMidpoint(road.startVertex, road.endVertex, calculateVertexPosition);

    const rectWidth = 60;
    const rectHeight = 10;

    const angle = calculateAngle(road.startVertex, road.endVertex, calculateVertexPosition);

    const rectX = midX - rectWidth / 2 ;
    const rectY = midY - rectHeight / 2;

    return (
        <rect
            x={rectX}
            y={rectY}
            width={rectWidth}
            height={rectHeight}
            fill={color}
            cursor="pointer"
            transform={`rotate(${angle}, ${midX}, ${midY})`}
        />
    );
};

const calculateMidpoint = (startVertex, endVertex, calculateVertexPosition) => {
    const [startX, startY] = calculateVertexPosition(startVertex.q, startVertex.r, startVertex.direction);
    const [endX, endY] = calculateVertexPosition(endVertex.q, endVertex.r, endVertex.direction);

    const midX = (startX + endX) / 2;
    const midY = (startY + endY) / 2;

    return [midX, midY];
};

const calculateAngle = (startVertex, endVertex, calculateVertexPosition) => {
    const [startX, startY] = calculateVertexPosition(startVertex.q, startVertex.r, startVertex.direction);
    const [endX, endY] = calculateVertexPosition(endVertex.q, endVertex.r, endVertex.direction);

    const deltaX = endX - startX;
    const deltaY = endY - startY;

    const angleInRadians = Math.atan2(deltaY, deltaX);
    const angleInDegrees = angleInRadians * (180 / Math.PI);

    return angleInDegrees;
};

export default Road;

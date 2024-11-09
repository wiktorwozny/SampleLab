import React from 'react';
import {Spinner} from "react-bootstrap";

interface LoadingOverlayProps {
    message: string;
}

const LoadingOverlay: React.FC<LoadingOverlayProps> = ({message}) => {
    return (
        <div className="rounded-3 fixed top-4 right-4 bg-white p-4 rounded shadow-lg flex items-center gap-4 !z-99">
            <Spinner animation="border" role="status" variant="primary">
            </Spinner>
            <span>{message}</span>
        </div>
    );
};

export default LoadingOverlay;

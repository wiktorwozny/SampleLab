import React from 'react';
import {Spinner} from "react-bootstrap";
import {useAppContext} from "../../contexts/AppContext";

interface LoadingOverlayProps {
    message: string;
}


const LoadingOverlay: React.FC<LoadingOverlayProps> = ({message}) => {

    const {isLoadingOverlayVisible} = useAppContext();


    return (
        isLoadingOverlayVisible ?
            <div className="rounded-3 fixed top-4 right-4 bg-white p-4 rounded shadow-lg flex items-center gap-4 !z-99">
                <Spinner animation="border" role="status" variant="primary">
                </Spinner>
                <span>{message}</span>
            </div> : <></>

    );
};

export default LoadingOverlay;

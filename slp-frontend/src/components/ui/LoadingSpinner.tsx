import {Spinner} from "react-bootstrap";
import React from "react";


export const LoadingSpinner = () => {
    return (
        <div className="flex flex-column justify-content-center align-items-center my-20">
            <Spinner animation="border" role="status" variant="primary">
            </Spinner>
            <span className="text-xl mt-5">Ładowanie...</span>
        </div>
    )
}
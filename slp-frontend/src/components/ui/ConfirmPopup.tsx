import React from 'react';
import {Modal} from "react-bootstrap";
import {StandardButton} from "./StandardButton";

interface ConfirmPopupProps {
    show: boolean;
    handleClose: () => void;
    onConfirm: () => void;
    message?: string
}

const ConfirmPopup: React.FC<ConfirmPopupProps> = ({
                                                       onConfirm, message, show,
                                                       handleClose,
                                                   }) => {


    const handleConfirm = () => {
        onConfirm();
        handleClose();
    }

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>
                    {"Potwierdź usunięcie"}
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {message != null ? (
                        <p className="text-xl my-3 text-gray-600">
                            {message}
                        </p>
                    ) :
                    (
                        <p className="text-xl my-3 text-gray-600">
                            Czy na pewno chcesz usunąć rekord?
                            Tej operacji nie można cofnąć.
                        </p>
                    )}
                <Modal.Footer>
                    <StandardButton
                        type={"submit"}
                        className="bg-blue-600 text-white font-semibold py-2 px-4 rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                        onClick={handleConfirm}
                    >
                        Usuń
                    </StandardButton>
                    <StandardButton
                        type={"button"}
                        className="bg-gray-600 text-white font-semibold py-2 px-4 rounded hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-opacity-50"
                        onClick={handleClose}
                    >
                        Anuluj
                    </StandardButton>

                </Modal.Footer>
            </Modal.Body>
        </Modal>
    );
};

export default ConfirmPopup;

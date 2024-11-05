import React from 'react';
import {Modal} from "react-bootstrap";
import {CancelButton, StandardButton} from "./StandardButton";

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
                        type={"button"}
                        className="!bg-red-500 hover:!bg-red-600"
                        onClick={handleConfirm}
                    >
                        Usuń
                    </StandardButton>
                    <CancelButton
                        type={"button"}
                        className="!border-red-500 hover:!bg-red-100 !text-red-500"
                        onClick={handleClose}
                    >
                        Anuluj
                    </CancelButton>

                </Modal.Footer>
            </Modal.Body>
        </Modal>
    );
};

export default ConfirmPopup;

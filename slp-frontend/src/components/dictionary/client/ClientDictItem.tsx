import React, {useContext, useEffect, useState} from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import {Address, Client} from '../../../utils/types';
import {addClient, updateClient} from "../../../helpers/clientApi";
import {AlertContext} from "../../../contexts/AlertsContext";

interface ClientDictItemProps {
    refresh: () => void;
    show: boolean;
    handleClose: () => void;
    item: Client | null;
    isView: boolean;
    isAdd: boolean;
    isEdit: boolean;
}

const ClientDictItem: React.FC<ClientDictItemProps> = ({
                                                           refresh,
                                                           show,
                                                           handleClose,
                                                           item,
                                                           isView,
                                                           isAdd,
                                                           isEdit,
                                                       }) => {
    const [formData, setFormData] = useState<Client>({
        id: null,
        name: '',
        wijharsCode: '',
        address: {
            street: '',
            zipCode: '',
            city: '',
        } as Address,
    });
    const {setAlertDetails} = useContext(AlertContext);

    useEffect(() => {
        if (item) {
            setFormData(item);
        }
    }, [item]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData((prevFormData) => ({
            ...prevFormData,
            [name]: value,
        }));
    };

    const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData((prevFormData) => ({
            ...prevFormData,
            address: {
                ...prevFormData.address,
                [name]: value,
            },
        }));
    };

    const handleEdit = async () => {
        try {
            let response = await updateClient(formData)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Edytowano definicję", type: "success"})
                refresh();
                handleClose();
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
        }
    };
    // Close modal after saving

    const handleAdd = async () => {
        try {
            let response = await addClient(formData)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Dodano nową definicję", type: "success"})
                refresh();
                handleClose();
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
        }
        // Close modal after saving
    };

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>
                    {isView ? 'Client Details' : isEdit ? 'Edit Client' : 'Add New Client'}
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3" controlId="formClientId">
                        <Form.Label column={true}>ID</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="ID"
                            value={formData.id ?? ''}
                            name="id"
                            disabled
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formClientName">
                        <Form.Label column={true}>Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Name"
                            value={formData.name}
                            name="name"
                            onChange={handleInputChange}
                            disabled={isView}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formClientWijharsCode">
                        <Form.Label column={true}>WIJHARS Code</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="WIJHARS Code"
                            value={formData.wijharsCode}
                            name="wijharsCode"
                            onChange={handleInputChange}
                            disabled={isView}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formClientAddressStreet">
                        <Form.Label column={true}>Street</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Street"
                            value={formData.address.street}
                            name="street"
                            onChange={handleAddressChange}
                            disabled={isView}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formClientAddressZipCode">
                        <Form.Label column={true}>Zip Code</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Zip Code"
                            value={formData.address.zipCode}
                            name="zipCode"
                            onChange={handleAddressChange}
                            disabled={isView}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formClientAddressCity">
                        <Form.Label column={true}>City</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="City"
                            value={formData.address.city}
                            name="city"
                            onChange={handleAddressChange}
                            disabled={isView}
                        />
                    </Form.Group>

                    {/* Add more form fields as needed */}
                </Form>
            </Modal.Body>
            <Modal.Footer>
                {(isEdit || isAdd) && (
                    <Button variant="primary" onClick={isEdit ? handleEdit : handleAdd}>
                        Save
                    </Button>
                )}
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ClientDictItem;
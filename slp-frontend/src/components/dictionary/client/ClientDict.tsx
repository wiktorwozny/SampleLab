import React, {useContext, useEffect, useState} from 'react';
import {deleteClient, getAllClients} from "../../../helpers/clientApi";
import {Address, Client, Column} from "../../../utils/types";
import DictionaryTable from "../../ui/DictionaryTable";
import ClientDictItem from "./ClientDictItem";
import {Button} from "react-bootstrap";
import {AlertContext} from "../../../contexts/AlertsContext";
import {CancelButton} from "../../ui/StandardButton";
import {useNavigate} from 'react-router-dom'; // Import useNavigate

const formatAddress = (address: number | string | Address): string => {
    if (typeof address === 'object' && address !== null && 'street' in address && 'zipCode' in address && 'city' in address) {
        return `ul.${address.street}, ${address.zipCode}, ${address.city}`;
    }
    return '';
};

const columns: Column<Client>[] = [
    {header: 'ID', accessor: 'id'},
    {header: 'Name', accessor: 'name'},
    {header: 'WIJHARS Code', accessor: 'wijharsCode'},
    {
        header: 'Address',
        accessor: 'address',
        render: (value) => value ? formatAddress(value) : '',
    },
];

const ClientDict = () => {
    const [clientsList, setClientsList] = useState<Client[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<Client | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);

    const navigate = useNavigate(); // Use the useNavigate hook

    const handleView = (item: Client) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(true);
        setIsAddMode(false);
        setIsEditMode(false);
    };

    const handleEdit = (item: Client) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(false);
        setIsAddMode(false);
        setIsEditMode(true);
    };

    const handleAdd = () => {
        setSelectedItem(null);  // No item selected since we're adding a new client
        setOpenModal(true);
        setIsViewMode(false);
        setIsAddMode(true);
        setIsEditMode(false);
    };

    const handleDelete = async (item: Client) => {
        try {
            let response = await deleteClient(item?.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getClients();
                handleClose();
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
        }
    };

    const handleClose = () => {
        setOpenModal(false);
    }

    const copyObject = (item: Client): Client => {
        return JSON.parse(JSON.stringify(item));
    };

    const getClients = () => {
        getAllClients().then((res) => {
            if (res.status === 200) {
                setClientsList(res.data);
            }
        })
    };

    useEffect(() => {
        getClients();
    }, []);

    return (
        <div className="w-full">
            <h1 className="text-center font-bold text-3xl w-full my-3">Klienci</h1>

            <div className="w-full justify-content-between flex mb-2">
                <Button className="self-center h-10 ml-2" variant="primary" onClick={handleAdd}>
                    Dodaj nowy
                </Button>
            </div>

            <DictionaryTable<Client>
                columns={columns}
                data={clientsList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={handleDelete}
            />
            <CancelButton
                type='button'
                className='mt-3'
                onClick={() => navigate(-1)} // Go back to the previous screen
            >Powrót</CancelButton>
            <ClientDictItem
                refresh={getClients}
                show={openModal}
                handleClose={handleClose}
                item={selectedItem}
                isView={isViewMode}
                isAdd={isAddMode}
                isEdit={isEditMode}
            />
        </div>
    );
};

export default ClientDict;

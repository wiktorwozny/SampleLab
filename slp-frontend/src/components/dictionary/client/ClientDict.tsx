import React, {useContext, useEffect, useState} from 'react';
import {deleteClient, getAllClients} from "../../../helpers/clientApi";
import {Address, Client, Column} from "../../../utils/types";
import DictionaryTable from "../../ui/DictionaryTable";
import ClientDictItem from "./ClientDictItem";
import {AlertContext} from "../../../contexts/AlertsContext";
import {CancelButton, StandardButton} from "../../ui/StandardButton";
import {useNavigate} from 'react-router-dom';
import ConfirmPopup from "../../ui/ConfirmPopup"; // Import useNavigate

const formatAddress = (address: number | string | Address): string => {
    if (typeof address === 'object' && address !== null && 'street' in address && 'zipCode' in address && 'city' in address) {
        return `ul.${address.street}, ${address.zipCode}, ${address.city}`;
    }
    return '';
};

const columns: Column<Client>[] = [
    {header: 'ID', accessor: 'id'},
    {header: 'Nazwa', accessor: 'name'},
    {header: 'Kod WIJHARS', accessor: 'wijharsCode'},
    {
        header: 'Adres',
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
    const [openConfirmPopup, setOpenConfirmPopup] = useState(false);
    const [itemToDelete, setItemToDelete] = useState<Client | null>(null);

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
        setSelectedItem(null);
        setOpenModal(true);
        setIsViewMode(false);
        setIsAddMode(true);
        setIsEditMode(false);
    };

    const handleDelete = async () => {
        try {
            let response = await deleteClient(itemToDelete!.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getClients();
                handleClose();
            }
        } catch (err: any) {
            console.log(err)
            if (err?.response?.status === 409) {
                setAlertDetails({isAlert: true, message: "Nie można usunąć rekordu, ponieważ zależą od niego inne dane", type: "error"})
            } else {
                setAlertDetails({isAlert: true, message: "Wystąpił bład, spróbuj ponownie później", type: "error"})
            }
        }
    };

    const confirmDelete = (item: Client) => {
        setItemToDelete(item);
        setOpenConfirmPopup(true);
    };

    const handleCloseConfirmPopup = () => {
        setOpenConfirmPopup(false);
        setItemToDelete(null);
    }

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
                <StandardButton className="self-center h-10 ml-2" type={"button"} onClick={handleAdd}>
                    Dodaj nowy
                </StandardButton>
            </div>

            <DictionaryTable<Client>
                columns={columns}
                data={clientsList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={confirmDelete}
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

            <ConfirmPopup
                onConfirm={handleDelete}
                show={openConfirmPopup}
                handleClose={handleCloseConfirmPopup}
            />
        </div>
    );
};

export default ClientDict;

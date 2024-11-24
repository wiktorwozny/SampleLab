import React, {useContext, useEffect, useState} from "react";
import {Column, Indication} from "../../../utils/types";
import {AlertContext} from "../../../contexts/AlertsContext";
import {deleteIndication, getAllIndications} from "../../../helpers/indicationApi";
import DictionaryTable from "../../ui/DictionaryTable";
import IndicationDictItem from "./IndicationDictItem";
import {CancelButton, StandardButton} from "../../ui/StandardButton";
import {useNavigate} from "react-router-dom";
import ConfirmPopup from "../../ui/ConfirmPopup";

const columns: Column<Indication>[] = [
    {header: 'ID', accessor: 'id'},
    {header: 'Nazwa', accessor: 'name'},
    {header: 'Metoda', accessor: 'method'},
    {header: 'Jednostka', accessor: 'unit'},
    {header: 'Laboratorium', accessor: 'laboratory'}
];

const IndicationDict = () => {

    const [indicationList, setIndicationList] = useState<Indication[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<Indication | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate();
    const [openConfirmPopup, setOpenConfirmPopup] = useState(false);
    const [itemToDelete, setItemToDelete] = useState<Indication | null>(null);


    const handleView = (item: Indication) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(true);
        setIsAddMode(false);
        setIsEditMode(false);
    };

    const handleEdit = (item: Indication) => {
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


    const handleDelete = async () => {
        try {
            let response = await deleteIndication(itemToDelete!.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getIndications();
                handleClose();
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"})
        }
    };

    const confirmDelete = (item: Indication) => {
        setItemToDelete(item);
        setOpenConfirmPopup(true);
    };

    const handleCloseConfirmPopup = () => {
        setOpenConfirmPopup(false);
    }

    const handleClose = () => {
        setOpenModal(false);
        setItemToDelete(null);
    }


    const copyObject = (item: Indication): Indication => {
        return JSON.parse(JSON.stringify(item));
    };

    const getIndications = () => {
        getAllIndications().then((res) => {
            if (res.status === 200) {
                setIndicationList(res.data);
            }
        })
    };

    useEffect(() => {
        getIndications();
    }, []);

    return (
        <div className="w-full">
            <h1 className="text-center font-bold text-3xl w-full my-3">Test dict</h1>

            <div className="w-full justify-content-between flex mb-2">
                <StandardButton className="self-center h-10 ml-2" type={"button"} onClick={handleAdd}>
                    Dodaj nowy
                </StandardButton>
            </div>

            <DictionaryTable<Indication>
                columns={columns}
                data={indicationList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={confirmDelete}
            />
            <CancelButton
                type='button'
                className='mt-3'
                onClick={() => navigate(-1)} // Go back to the previous screen
            >Powrót</CancelButton>

            <IndicationDictItem
                refresh={getIndications}
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
    )
}

export default IndicationDict
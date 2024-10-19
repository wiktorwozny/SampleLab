import {Column, Inspection} from "../../../utils/types";
import React, {useContext, useEffect, useState} from "react";
import {AlertContext} from "../../../contexts/AlertsContext";
import {Button} from "react-bootstrap";
import DictionaryTable from "../../ui/DictionaryTable";
import {deleteInspection, getAllInspection} from "../../../helpers/inspectionApi";
import InspectionDictItem from "./InspectionDictItem";
import {CancelButton} from "../../ui/StandardButton";
import {useNavigate} from "react-router-dom";
import ConfirmPopup from "../../ui/ConfirmPopup";


const columns: Column<Inspection>[] = [
    {header: 'ID', accessor: 'id'},
    {header: 'Nazwa', accessor: 'name'},
];

const InspectionDict = () => {
    const [inspectionList, setInspectionList] = useState<Inspection[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<Inspection | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate();

    const [openConfirmPopup, setOpenConfirmPopup] = useState(false);
    const [itemToDelete, setItemToDelete] = useState<Inspection | null>(null);

    const handleView = (item: Inspection) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(true);
        setIsAddMode(false);
        setIsEditMode(false);
    };

    const handleEdit = (item: Inspection) => {
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
            let response = await deleteInspection(itemToDelete!.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getInspections();
                handleClose();
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
        }
    };

    const confirmDelete = (item: Inspection) => {
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

    const copyObject = (item: Inspection): Inspection => {
        return JSON.parse(JSON.stringify(item));
    };

    const getInspections = () => {
        getAllInspection().then((res) => {
            if (res.status === 200) {
                setInspectionList(res.data);
            }
        })
    };

    useEffect(() => {
        getInspections();
    }, []);

    return (
        <div className="w-full">
            <h1 className="text-center font-bold text-3xl w-full my-3">Rodzaje kontroli</h1>

            <div className="w-full justify-content-between flex mb-2">
                <Button className="self-center h-10 ml-2" variant="primary" onClick={handleAdd}>
                    Dodaj nowy
                </Button>

            </div>

            <DictionaryTable<Inspection>
                columns={columns}
                data={inspectionList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={confirmDelete}
            />
            <CancelButton
                type='button'
                className='mt-3'
                onClick={() => navigate(-1)} // Go back to the previous screen
            >Powrót</CancelButton>
            <InspectionDictItem
                refresh={getInspections}
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
export default InspectionDict
import {Column, SamplingStandards} from "../../../utils/types";
import React, {useContext, useEffect, useState} from "react";
import {AlertContext} from "../../../contexts/AlertsContext";
import DictionaryTable from "../../ui/DictionaryTable";
import {deleteSamplingStandard, getAllSamplingStandard} from "../../../helpers/samplingStandardApi";
import SamplingStandardDictItem from "./SamplingStandardDictItem";
import {CancelButton, StandardButton} from "../../ui/StandardButton";
import {useNavigate} from "react-router-dom";
import ConfirmPopup from "../../ui/ConfirmPopup";


const columns: Column<SamplingStandards>[] = [
    {header: 'ID', accessor: 'id', className: 'w-12'},
    {header: 'Nazwa', accessor: 'name'},
];

const SamplingStandardDict = () => {
    const [samplingStandardsList, setSamplingStandardsList] = useState<SamplingStandards[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<SamplingStandards | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate();
    const [openConfirmPopup, setOpenConfirmPopup] = useState(false);
    const [itemToDelete, setItemToDelete] = useState<SamplingStandards | null>(null);

    const handleView = (item: SamplingStandards) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(true);
        setIsAddMode(false);
        setIsEditMode(false);
    };

    const handleEdit = (item: SamplingStandards) => {
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
            let response = await deleteSamplingStandard(itemToDelete!.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getSamplingStandards();
                handleClose();
            }
        } catch (err: any) {
            console.log(err)
            if (err?.response?.status === 409) {
                setAlertDetails({
                    isAlert: true,
                    message: "Nie można usunąć rekordu, ponieważ zależą od niego inne dane",
                    type: "error"
                })
            } else {
                setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"})
            }
        }
    };

    const confirmDelete = (item: SamplingStandards) => {
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

    const copyObject = (item: SamplingStandards): SamplingStandards => {
        return JSON.parse(JSON.stringify(item));
    };

    const getSamplingStandards = () => {
        getAllSamplingStandard().then((res) => {
            if (res.status === 200) {
                setSamplingStandardsList(res.data);
            }
        })
    };

    useEffect(() => {
        getSamplingStandards();
    }, []);

    return (
        <div className="w-full">
            <h1 className="text-center font-bold text-3xl w-full my-3">Normy pobrania próbki</h1>

            <div className="w-full justify-content-between flex mb-2">
                <StandardButton className="self-center h-10 ml-2" type={"button"} onClick={handleAdd}>
                    Dodaj nowy
                </StandardButton>
            </div>

            <DictionaryTable<SamplingStandards>
                columns={columns}
                data={samplingStandardsList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={confirmDelete}
            />
            <CancelButton
                type='button'
                className='mt-3'
                onClick={() => navigate(-1)} // Go back to the previous screen
            >Powrót</CancelButton>
            <SamplingStandardDictItem
                refresh={getSamplingStandards}
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
export default SamplingStandardDict
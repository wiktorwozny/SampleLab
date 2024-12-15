import React, {useContext, useEffect, useState} from "react";
import {Assortment, Column, Indication, ProductGroup} from "../../../utils/types";
import {AlertContext} from "../../../contexts/AlertsContext";
import {useNavigate} from "react-router-dom";
import DictionaryTable from "../../ui/DictionaryTable";
import {CancelButton, StandardButton} from "../../ui/StandardButton";
import ConfirmPopup from "../../ui/ConfirmPopup";
import {deleteAssortment, getAllAssortments} from "../../../helpers/assortmentApi";
import AssortmentDictItem from "./AssortmentDictItem";
import Title from "../../ui/Title";

const columns: Column<Assortment>[] = [
    {header: 'ID', accessor: 'id', className: 'w-12'},
    {header: 'Nazwa', accessor: 'name'},
    {header: 'Metoda organoleptyczna', accessor: 'organolepticMethod'},
    {
        header: 'Grupa', accessor: 'group', render: (value) => {
            if (typeof value === 'object' && value !== null && 'name' in value) {
                return (value as ProductGroup).name;
            }
            return '';
        }
    },
    {
        header: 'Oznaczenia', accessor: 'indications', render: (value) => {
            if (Array.isArray(value)) {
                return value.map((indication: Indication) => indication.name).join(' ,  ');
            }
            return '';
        }
    },
];

const AssortmentDict = () => {
    const [assortmentList, setAssortmentList] = useState<Assortment[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<Assortment | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate();
    const [openConfirmPopup, setOpenConfirmPopup] = useState(false);
    const [itemToDelete, setItemToDelete] = useState<Assortment | null>(null);

    const handleView = (item: Assortment) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(true);
        setIsAddMode(false);
        setIsEditMode(false);
    };

    const handleEdit = (item: Assortment) => {
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
            let response = await deleteAssortment(itemToDelete!.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getAssortmentsList();
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

    const confirmDelete = (item: Assortment) => {
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

    const copyObject = (item: Assortment): Assortment => {
        return JSON.parse(JSON.stringify(item));
    };

    const getAssortmentsList = () => {
        getAllAssortments().then((res) => {
            if (res.status === 200) {
                setAssortmentList(res.data);
            }
        })
    };


    useEffect(() => {
        getAssortmentsList();
    }, []);

    return (
        <div className="w-full">
            <Title message={'Asortymenty'}/>

            <div className="w-full justify-content-between flex mb-2">
                <StandardButton className="self-center h-10 ml-2" type={"button"} onClick={handleAdd}>
                    Dodaj nowy
                </StandardButton>
            </div>

            <DictionaryTable<Assortment>
                columns={columns}
                data={assortmentList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={confirmDelete}
                maxRows={5}
            />
            <CancelButton
                type='button'
                className='mt-3'
                onClick={() => navigate(-1)} // Go back to the previous screen
            >Powrót</CancelButton>

            <AssortmentDictItem
                refresh={getAssortmentsList}
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
export default AssortmentDict;
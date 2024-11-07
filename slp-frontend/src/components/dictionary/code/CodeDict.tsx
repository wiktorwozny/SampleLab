import {Code, Column} from "../../../utils/types";
import React, {useContext, useEffect, useState} from "react";
import {AlertContext} from "../../../contexts/AlertsContext";
import {deleteCode, getAllCodes} from "../../../helpers/codeApi";
import DictionaryTable from "../../ui/DictionaryTable";
import CodeDictItem from "./CodeDictItem";
import {CancelButton, StandardButton} from "../../ui/StandardButton";
import {useNavigate} from "react-router-dom";
import ConfirmPopup from "../../ui/ConfirmPopup";


const columns: Column<Code>[] = [
    {header: 'ID', accessor: 'id'},
    {header: 'Nazwa', accessor: 'name'},
];

const CodeDict = () => {
    const [codeList, setCodeList] = useState<Code[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [openConfirmPopup, setOpenConfirmPopup] = useState(false);
    const [selectedItem, setSelectedItem] = useState<Code | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate();
    const [itemToDelete, setItemToDelete] = useState<Code | null>(null);

    const handleView = (item: Code) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(true);
        setIsAddMode(false);
        setIsEditMode(false);
    };

    const handleEdit = (item: Code) => {
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
            const response = await deleteCode(itemToDelete!.id);
            if (response.status === 200 || response.status === 201) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"});
                getCodes();
                handleClose();
            }
        } catch (err) {
            console.error(err);
            setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"});
        }
    };

    const confirmDelete = (item: Code) => {
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


    const copyObject = (item: Code): Code => {
        return JSON.parse(JSON.stringify(item));
    };

    const getCodes = () => {
        getAllCodes().then((res) => {
            if (res.status === 200) {
                setCodeList(res.data);
            }
        })
    };

    useEffect(() => {
        getCodes();
    }, []);

    return (
        <div className="w-full">
            <h1 className="text-center font-bold text-3xl w-full my-3">Kody</h1>

            <div className="w-full justify-content-between flex mb-2">
                <StandardButton className="self-center h-10 ml-2" type={"button"} onClick={handleAdd}>
                    Dodaj nowy
                </StandardButton>
            </div>

            <DictionaryTable<Code>
                columns={columns}
                data={codeList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={confirmDelete}
            />
            <CancelButton
                type='button'
                className='mt-3'
                onClick={() => navigate(-1)} // Go back to the previous screen
            >Powrót</CancelButton>

            <CodeDictItem
                refresh={getCodes}
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
export default CodeDict
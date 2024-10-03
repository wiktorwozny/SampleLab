import {Code, Column} from "../../../utils/types";
import React, {useContext, useEffect, useState} from "react";
import {AlertContext} from "../../../contexts/AlertsContext";
import {deleteCode, getAllCodes} from "../../../helpers/codeApi";
import {Button} from "react-bootstrap";
import DictionaryTable from "../../ui/DictionaryTable";
import CodeDictItem from "./CodeDictItem";
import {CancelButton} from "../../ui/StandardButton";
import {useNavigate} from "react-router-dom";


const columns: Column<Code>[] = [
    {header: 'ID', accessor: 'id'},
    {header: 'Name', accessor: 'name'},
];

const CodeDict = () => {
    const [codeList, setCodeList] = useState<Code[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<Code | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate();
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

    const handleDelete = async (item: Code) => {
        try {
            let response = await deleteCode(item?.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getCodes();
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
                <Button className="self-center h-10 ml-2" variant="primary" onClick={handleAdd}>
                    Dodaj nowy
                </Button>

            </div>

            <DictionaryTable<Code>
                columns={columns}
                data={codeList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={handleDelete}
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
        </div>
    )
}
export default CodeDict
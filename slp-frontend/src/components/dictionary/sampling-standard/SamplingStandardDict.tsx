import {Column, SamplingStandards} from "../../../utils/types";
import React, {useContext, useEffect, useState} from "react";
import {AlertContext} from "../../../contexts/AlertsContext";
import {Button} from "react-bootstrap";
import DictionaryTable from "../../ui/DictionaryTable";
import {deleteSamplingStandard, getAllSamplingStandard} from "../../../helpers/samplingStandardApi";
import SamplingStandardDictItem from "./SamplingStandardDictItem";
import {CancelButton} from "../../ui/StandardButton";
import {useNavigate} from "react-router-dom";


const columns: Column<SamplingStandards>[] = [
    {header: 'ID', accessor: 'id'},
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

    const handleDelete = async (item: SamplingStandards) => {
        try {
            let response = await deleteSamplingStandard(item?.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getSamplingStandards();
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
            <h1 className="text-center font-bold text-3xl w-full my-3">Standardy póbek</h1>

            <div className="w-full justify-content-between flex mb-2">
                <Button className="self-center h-10 ml-2" variant="primary" onClick={handleAdd}>
                    Dodaj nowy
                </Button>

            </div>

            <DictionaryTable<SamplingStandards>
                columns={columns}
                data={samplingStandardsList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={handleDelete}
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
        </div>
    )
}
export default SamplingStandardDict
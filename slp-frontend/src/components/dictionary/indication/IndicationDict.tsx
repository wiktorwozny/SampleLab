import React, {useContext, useEffect, useState} from "react";
import {Column, Indication} from "../../../utils/types";
import {AlertContext} from "../../../contexts/AlertsContext";
import {deleteIndication, getAllIndications} from "../../../helpers/indicationApi";
import {Button} from "react-bootstrap";
import DictionaryTable from "../../ui/DictionaryTable";
import IndicationDictItem from "./IndicationDictItem";

const columns: Column<Indication>[] = [
    {header: 'ID', accessor: 'id'},
    {header: 'Name', accessor: 'name'},
    {header: 'Norm', accessor: 'norm'},
    {header: 'Unit', accessor: 'unit'},
    {header: 'Laboratory', accessor: 'laboratory'}
];

const IndicationDict = () => {

    const [indicationList, setIndicationList] = useState<Indication[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<Indication | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);

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


    const handleDelete = async (item: Indication) => {
        try {
            let response = await deleteIndication(item?.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getIndications();
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

            <div className="w-full justify-content-between flex">
                <Button className="self-center h-10 ml-2" variant="primary" onClick={handleAdd}>
                    Dodaj nowy
                </Button>
                {/*<div*/}
                {/*    className="flex border relative mr-2 mb-2 p-2 border-black items-center hover:bg-gray-300 cursor-pointer"*/}
                {/*>*/}
                {/*    <div>Filtruj &nbsp;</div>*/}
                {/*    <HiAdjustmentsHorizontal className="text-3xl"></HiAdjustmentsHorizontal>*/}
                {/*</div>*/}

            </div>

            <DictionaryTable<Indication>
                columns={columns}
                data={indicationList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={handleDelete}
            />
            <IndicationDictItem
                refresh={getIndications}
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

export default IndicationDict
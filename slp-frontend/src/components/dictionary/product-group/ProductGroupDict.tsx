import {Column, ProductGroup} from "../../../utils/types";
import React, {useContext, useEffect, useState} from "react";
import {AlertContext} from "../../../contexts/AlertsContext";
import DictionaryTable from "../../ui/DictionaryTable";
import {CancelButton, StandardButton} from "../../ui/StandardButton";
import {useNavigate} from "react-router-dom";
import {deleteGroup, getAllGroup} from "../../../helpers/groupApi";
import ProductGroupDictItem from "./ProductGroupDictItem";
import ConfirmPopup from "../../ui/ConfirmPopup";


const columns: Column<ProductGroup>[] = [
    {header: 'ID', accessor: 'id', className: 'w-12'},
    {header: 'Nazwa', accessor: 'name'},
];

const ProductGroupDict = () => {
    const [productGroupList, setProductGroupList] = useState<ProductGroup[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<ProductGroup | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate();
    const [openConfirmPopup, setOpenConfirmPopup] = useState(false);
    const [itemToDelete, setItemToDelete] = useState<ProductGroup | null>(null);


    const handleView = (item: ProductGroup) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(true);
        setIsAddMode(false);
        setIsEditMode(false);
    };

    const handleEdit = (item: ProductGroup) => {
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
            let response = await deleteGroup(itemToDelete!.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getProductGroups();
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
                setAlertDetails({isAlert: true, message: "Wystąpił bład, spróbuj ponownie później", type: "error"})
            }
        }
    };

    const confirmDelete = (item: ProductGroup) => {
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

    const copyObject = (item: ProductGroup): ProductGroup => {
        return JSON.parse(JSON.stringify(item));
    };

    const getProductGroups = () => {
        getAllGroup().then((res) => {
            if (res.status === 200) {
                setProductGroupList(res.data);
            }
        })
    };

    useEffect(() => {
        getProductGroups();
    }, []);

    return (
        <div className="w-full">
            <h1 className="text-center font-bold text-3xl w-full my-3">Grupy produktów</h1>

            {localStorage.getItem('role') !== 'INTERN' &&
            <div className="w-full justify-content-between flex mb-2">
                <StandardButton className="self-center h-10 ml-2" type={"button"} onClick={handleAdd}>
                    Dodaj nowy
                </StandardButton>
            </div>}

            <DictionaryTable<ProductGroup>
                columns={columns}
                data={productGroupList}
                onView={handleView}
                onEdit={handleEdit}
                onDelete={confirmDelete}
            />
            <CancelButton
                type='button'
                className='mt-3'
                onClick={() => navigate(-1)} // Go back to the previous screen
            >Powrót</CancelButton>
            <ProductGroupDictItem
                refresh={getProductGroups}
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
                message="Czy na pewno chcesz usunąć grupę wraz z asortymentem? Tej operacji nie można cofnąć."
            />
        </div>
    )
}
export default ProductGroupDict

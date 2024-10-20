// import {Column, ProductGroup} from "../../../utils/types";
// import React, {useContext, useEffect, useState} from "react";
// import {AlertContext} from "../../../contexts/AlertsContext";
// import {Button} from "react-bootstrap";
// import DictionaryTable from "../../ui/DictionaryTable";
// import {CancelButton} from "../../ui/StandardButton";
// import {useNavigate} from "react-router-dom";
// import {deleteGroup, getAllGroup} from "../../../helpers/groupApi";
// import ProductGroupDictItem from "./ProductGroupDictItem";


// const columns: Column<ProductGroup>[] = [
//     {header: 'ID', accessor: 'id'},
//     {header: 'Nazwa', accessor: 'name'},
// ];

// const ProductGroupDict = () => {
//     const [productGroupList, setProductGroupList] = useState<ProductGroup[]>([]);
//     const {setAlertDetails} = useContext(AlertContext);
//     const [openModal, setOpenModal] = useState(false);
//     const [selectedItem, setSelectedItem] = useState<ProductGroup | null>(null);
//     const [isViewMode, setIsViewMode] = useState(false);
//     const [isAddMode, setIsAddMode] = useState(false);
//     const [isEditMode, setIsEditMode] = useState(false);
//     const navigate = useNavigate();
//     const handleView = (item: ProductGroup) => {
//         setSelectedItem(copyObject(item));
//         setOpenModal(true);
//         setIsViewMode(true);
//         setIsAddMode(false);
//         setIsEditMode(false);
//     };

//     const handleEdit = (item: ProductGroup) => {
//         setSelectedItem(copyObject(item));
//         setOpenModal(true);
//         setIsViewMode(false);
//         setIsAddMode(false);
//         setIsEditMode(true);
//     };

//     const handleAdd = () => {
//         setSelectedItem(null);  // No item selected since we're adding a new client
//         setOpenModal(true);
//         setIsViewMode(false);
//         setIsAddMode(true);
//         setIsEditMode(false);
//     };

//     const handleDelete = async (item: ProductGroup) => {
//         try {
//             let response = await deleteGroup(item?.id)
//             console.log(response)
//             if (response.status === 201 || response.status === 200) {
//                 setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
//                 getProductGroups();
//                 handleClose();
//             }
//         } catch (err) {
//             console.log(err)
//             setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
//         }
//     };

//     const handleClose = () => {
//         setOpenModal(false);
//     }

//     const copyObject = (item: ProductGroup): ProductGroup => {
//         return JSON.parse(JSON.stringify(item));
//     };

//     const getProductGroups = () => {
//         getAllGroup().then((res) => {
//             if (res.status === 200) {
//                 setProductGroupList(res.data);
//             }
//         })
//     };

//     useEffect(() => {
//         getProductGroups();
//     }, []);

//     return (
//         <div className="w-full">
//             <h1 className="text-center font-bold text-3xl w-full my-3">Grupy produktów</h1>

//             <div className="w-full justify-content-between flex mb-2">
//                 <Button className="self-center h-10 ml-2" variant="primary" onClick={handleAdd}>
//                     Dodaj nowy
//                 </Button>

//             </div>

//             <DictionaryTable<ProductGroup>
//                 columns={columns}
//                 data={productGroupList}
//                 onView={handleView}
//                 onEdit={handleEdit}
//                 onDelete={handleDelete}
//             />
//             <CancelButton
//                 type='button'
//                 className='mt-3'
//                 onClick={() => navigate(-1)} // Go back to the previous screen
//             >Powrót</CancelButton>
//             <ProductGroupDictItem
//                 refresh={getProductGroups}
//                 show={openModal}
//                 handleClose={handleClose}
//                 item={selectedItem}
//                 isView={isViewMode}
//                 isAdd={isAddMode}
//                 isEdit={isEditMode}
//             />
//         </div>
//     )
// }
// export default ProductGroupDict
export {}
// import React, {useContext, useEffect, useState} from "react";
// import {Indication, ProductGroup, SamplingStandards} from "../../../utils/types";
// import {FormProvider, useForm} from "react-hook-form";
// import {AlertContext} from "../../../contexts/AlertsContext";
// import {Button, Modal} from "react-bootstrap";
// import {FormLabel} from "../../ui/Labels";
// import {Input} from "../../ui/Input";
// import {getAllIndications} from "../../../helpers/indicationApi";
// import {getAllSamplingStandard} from "../../../helpers/samplingStandardApi";
// import {ModalSelection} from "../../ui/ModalSelection"; // Import modal component

// interface ProductGroupDictItemProps {
//     refresh: () => void;
//     show: boolean;
//     handleClose: () => void;
//     item: ProductGroup | null;
//     isView: boolean;
//     isAdd: boolean;
//     isEdit: boolean;
// }

// const ProductGroupDictItem: React.FC<ProductGroupDictItemProps> = ({
//                                                                        refresh,
//                                                                        show,
//                                                                        handleClose,
//                                                                        item,
//                                                                        isView,
//                                                                        isAdd,
//                                                                        isEdit,
//                                                                    }) => {
//     const [indicationsList, setIndicationsList] = useState<Indication[]>([]);
//     const [samplingStandardsList, setSamplingStandardsList] = useState<SamplingStandards[]>([]);

//     const method = useForm();
//     const {reset, handleSubmit, register, formState: {errors}} = method
//     const {setAlertDetails} = useContext(AlertContext);


//     const [selectedIndications, setSelectedIndications] = useState<number[]>(item?.indications?.map(i => i.id) || []);
//     const [selectedSamplingStandards, setSelectedSamplingStandards] = useState<number[]>(item?.samplingStandards?.map(s => s.id) || []);

//     // Local lists to display selected items
//     const [selectedIndicationsList, setSelectedIndicationsList] = useState<Indication[]>(item?.indications || []);
//     const [selectedSamplingStandardsList, setSelectedSamplingStandardsList] = useState<SamplingStandards[]>(item?.samplingStandards || []);

//     const [showIndicationsModal, setShowIndicationsModal] = useState(false);
//     const [showSamplingStandardsModal, setShowSamplingStandardsModal] = useState(false);


//     useEffect(() => {
//         if (item !== null) {
//             reset(item);
//             setSelectedIndications(item?.indications?.map(i => i.id) || []);
//             setSelectedSamplingStandards(item?.samplingStandards?.map(s => s.id) || []);
//             setSelectedIndicationsList(item?.indications || []);
//             setSelectedSamplingStandardsList(item?.samplingStandards || []);
//         } else {
//             resetForm();
//         }
//     }, [item]);

//     const resetForm = () => {
//         reset({id: '', name: ''});
//         setSelectedIndications([]);
//         setSelectedSamplingStandards([]);
//         setSelectedIndicationsList([]);
//         setSelectedSamplingStandardsList([]);
//     };

//     const handleEdit = (formData: any) => {
//         formData.indications = selectedIndications;
//         formData.samplingStandards = selectedSamplingStandards;
//         console.log()
//         try {
//             updateGroup(formData).then((response) => {
//                 if (response.status === 201 || response.status === 200) {
//                     setAlertDetails({isAlert: true, message: "Edytowano definicję", type: "success"})
//                     refresh();
//                     handleClose();
//                 }
//             })
//         } catch (err) {
//             console.log(err)
//             setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
//         }
//     };

//     const handleAdd = (formData: any) => {
//         formData.indications = selectedIndications;
//         formData.samplingStandards = selectedSamplingStandards;


//         try {
//             addGroup(formData).then((response) => {
//                 if (response.status === 201 || response.status === 200) {
//                     setAlertDetails({isAlert: true, message: "Dodano nową definicję", type: "success"})
//                     refresh();
//                     handleClose();
//                 }
//             })
//         } catch (err) {
//             console.log(err)
//             setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
//         }
//     };

//     const getIndications = () => {
//         getAllIndications().then((res) => {
//             if (res.status === 200) {
//                 setIndicationsList(res.data);
//             }
//         })
//     };

//     const getSamplingStandards = () => {
//         getAllSamplingStandard().then((res) => {
//             if (res.status === 200) {
//                 setSamplingStandardsList(res.data);
//             }
//         })
//     };

//     useEffect(() => {
//         getIndications();
//         getSamplingStandards();
//     }, []);

//     const submit = (formData: any) => {
//         if (isEdit) {
//             handleEdit(formData);
//         } else {
//             handleAdd(formData);
//         }
//         resetForm();
//     };

//     const handleCancel = () => {
//         handleClose();
//         resetForm();
//     };

//     const handleSaveIndications = (selected: number[]) => {
//         setSelectedIndications(selected);
//         const selectedIndicationsData = indicationsList.filter(indication => selected.includes(indication.id));
//         setSelectedIndicationsList(selectedIndicationsData);  // Update local list for display
//     };

//     const handleSaveSamplingStandards = (selected: number[]) => {
//         setSelectedSamplingStandards(selected);
//         const selectedSamplingStandardsData = samplingStandardsList.filter(standard => selected.includes(standard.id));
//         setSelectedSamplingStandardsList(selectedSamplingStandardsData);  // Update local list for display
//     };

//     return (
//         <Modal show={show} onHide={handleClose}>
//             <FormProvider {...method}>
//                 <form className="bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
//                     <Modal.Header closeButton>
//                         <Modal.Title>
//                             {isView ? 'Szczegóły' : isEdit ? 'Edycja' : 'Nowy'}
//                         </Modal.Title>
//                     </Modal.Header>
//                     <Modal.Body>
//                         <div hidden={!isView}>
//                             <FormLabel>ID</FormLabel>
//                             <Input type="text" disabled={true} {...register("id", {
//                                 required: {
//                                     value: false,
//                                     message: "Pole wymagane"
//                                 }
//                             })}
//                             />
//                             {errors.id && errors.id.message &&
//                                 <p className="text-red-600">{`${errors.id.message}`}</p>}
//                         </div>
//                         <FormLabel>Nazwa</FormLabel>
//                         <Input type="text" disabled={isView}
//                                placeholder="Nazwa" {...register("name", {
//                             required: {
//                                 value: true,
//                                 message: "Pole wymagane"
//                             }
//                         })}
//                         />
//                         {errors.name && errors.name.message &&
//                             <p className="text-red-600">{`${errors.name.message}`}</p>}


//                         {/* Buttons to open modal selection */}
//                         {!isView && (
//                             <div className="flex space-x-4 mt-4">
//                                 <Button variant="primary" onClick={() => setShowIndicationsModal(true)}>
//                                     Wybierz oznaczenia
//                                 </Button>
//                                 <Button variant="primary" onClick={() => setShowSamplingStandardsModal(true)}>
//                                     Wybierz normy pobrania
//                                 </Button>
//                             </div>
//                         )}

//                         <>
//                             <h5 className="mt-6 mb-2 text-lg font-semibold">Oznaczenia</h5>
//                             <div className="max-h-96 overflow-y-auto">
//                                 <table className="min-w-full table-auto border-collapse border border-gray-300">
//                                     <thead>
//                                     <tr className="bg-gray-100">
//                                         <th className="p-2 border border-gray-300 text-left">Nazwa</th>
//                                     </tr>
//                                     </thead>
//                                     <tbody>
//                                     {selectedIndicationsList?.map((option, index) => (
//                                         <tr
//                                             key={option.id}
//                                             className={`hover:bg-gray-100 ${index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}`}
//                                         >
//                                             <td className="p-2 border border-gray-300">{option.name}</td>
//                                         </tr>
//                                     ))}
//                                     </tbody>
//                                 </table>
//                             </div>

//                             <h5 className="mt-6 mb-2 text-lg font-semibold">Normy pobrania</h5>
//                             <div className="max-h-96 overflow-y-auto">
//                                 <table className="min-w-full table-auto border-collapse border border-gray-300">
//                                     <thead>
//                                     <tr className="bg-gray-100">
//                                         <th className="p-2 border border-gray-300 text-left">Nazwa</th>
//                                     </tr>
//                                     </thead>
//                                     <tbody>
//                                     {selectedSamplingStandardsList?.map((option, index) => (
//                                         <tr
//                                             key={option.id}
//                                             className={`hover:bg-gray-100 ${index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}`}
//                                         >
//                                             <td className="p-2 border border-gray-300">{option.name}</td>
//                                         </tr>
//                                     ))}
//                                     </tbody>
//                                 </table>
//                             </div>
//                         </>

//                     </Modal.Body>
//                     <Modal.Footer>
//                         {(isEdit || isAdd) && (
//                             <Button type="submit" variant="primary">
//                                 Zapisz
//                             </Button>
//                         )}
//                         <Button variant="secondary" onClick={handleCancel}>
//                             Anuluj
//                         </Button>
//                     </Modal.Footer>
//                 </form>
//             </FormProvider>

//             <ModalSelection
//                 title="Wybierz Indikacje"
//                 options={indicationsList}
//                 selectedOptions={selectedIndications}
//                 show={showIndicationsModal}
//                 handleClose={() => setShowIndicationsModal(false)}
//                 handleSave={handleSaveIndications}
//             />

//             {/* Modal for selecting Sampling Standards */}
//             <ModalSelection
//                 title="Wybierz Standardy Próbkowania"
//                 options={samplingStandardsList}
//                 selectedOptions={selectedSamplingStandards}
//                 show={showSamplingStandardsModal}
//                 handleClose={() => setShowSamplingStandardsModal(false)}
//                 handleSave={handleSaveSamplingStandards}
//             />
//         </Modal>
//     );
// };

// export default ProductGroupDictItem;
export {}
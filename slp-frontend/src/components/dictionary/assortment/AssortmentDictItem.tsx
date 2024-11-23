import React, {useContext, useEffect, useState} from "react";
import {FormProvider, useForm} from "react-hook-form";
import {AlertContext} from "../../../contexts/AlertsContext";
import {Button, Modal} from "react-bootstrap";
import {FormLabel} from "../../ui/Labels";
import {Input} from "../../ui/Input";
import {addAssortment, updateAssortment} from "../../../helpers/assortmentApi";
import {getAllGroup} from "../../../helpers/groupApi";
import SearchableDropdown from "../../ui/SearchableDropdown";
import {Assortment, Indication, ProductGroup} from "../../../utils/types";
import IndicationForAssortmentDictItem from "./IndicationForAssortmentDictItem";
import {FaTrashCan} from "react-icons/fa6";
import {StandardButton} from "../../ui/StandardButton";

interface AssortmentDictItemProps {
    refresh: () => void;
    show: boolean;
    handleClose: () => void;
    item: Assortment | null;
    isView: boolean;
    isAdd: boolean;
    isEdit: boolean;
}

const AssortmentDictItem: React.FC<AssortmentDictItemProps> = ({
                                                                   refresh,
                                                                   show,
                                                                   handleClose,
                                                                   item,
                                                                   isView,
                                                                   isAdd,
                                                                   isEdit,
                                                               }) => {
    const method = useForm();
    const {reset, handleSubmit, register, formState: {errors}, setValue} = method;
    const {setAlertDetails} = useContext(AlertContext);

    const [selectedIndicationList, setSelectedIndicationList] = useState<Indication[]>(item?.indications || []);
    const [showIndicationModal, setShowIndicationModal] = useState(false);
    const [productGroupList, setProductGroupList] = useState<ProductGroup[]>([]);
    const [selectedProductGroup, setSelectedProductGroup] = useState<ProductGroup | null>(item?.group || null);


    useEffect(() => {
        if (item !== null) {
            reset(item);
            setSelectedIndicationList(item.indications);
            setSelectedProductGroup(item.group);
        } else {
            resetForm();
        }
    }, [item]);

    const resetForm = () => {
        reset({id: '', name: '', organolepticMethod: '', group: ''});
        setSelectedIndicationList([]);
        setSelectedProductGroup(null);
    };

    const handleEdit = (formData: any) => {
        formData.indications = selectedIndicationList;
        formData.group = selectedProductGroup;
        console.log(formData);
        try {
            updateAssortment(formData).then((response) => {
                if (response.status === 201 || response.status === 200) {
                    setAlertDetails({isAlert: true, message: "Edytowano definicję", type: "success"});
                    refresh();
                    handleClose();
                }
            });
        } catch (err) {
            setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"});
        }
    };

    const handleAdd = (formData: any) => {
        formData.indications = selectedIndicationList;
        formData.group = selectedProductGroup;
        console.log(formData);
        try {
            addAssortment(formData).then((response) => {
                if (response.status === 201 || response.status === 200) {
                    setAlertDetails({isAlert: true, message: "Dodano nową definicję", type: "success"});
                    refresh();
                    handleClose();
                }
            });
        } catch (err) {
            setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"});
        }
    };

    const submit = (formData: any) => {
        if (isEdit) {
            handleEdit(formData);
        } else {
            handleAdd(formData);
        }
        resetForm();
    };

    const handleCancel = () => {
        handleClose();
        resetForm();
    };

    const getProductGroupList = () => {
        getAllGroup().then((res) => {
            if (res.status === 200) {
                setProductGroupList(res.data);
            }
        });
    };

    useEffect(() => {
        getProductGroupList();
    }, []);

    const addNewIndicationToList = (newIndication: Indication) => {
        setSelectedIndicationList((prevList) => [...prevList, newIndication]);
    };

    const removeIndicationFromList = (id: number) => {
        setSelectedIndicationList((prevList) => prevList.filter((indication) => indication.id !== id));
    };


    return (
        <Modal show={show} onHide={handleClose}>
            <FormProvider {...method}>
                <form className="bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
                    <Modal.Header closeButton>
                        <Modal.Title>
                            {isView ? 'Szczegóły' : isEdit ? 'Edycja' : 'Nowy'}
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div hidden={!isView}>
                            <FormLabel>ID</FormLabel>
                            <Input type="text" disabled={true} {...register("id")} />
                        </div>
                        <FormLabel>Nazwa</FormLabel>
                        <Input type="text" disabled={isView}
                               placeholder="Nazwa" {...register("name", {required: true})} />
                        {errors.name && <p className="text-red-600">{`${errors.name.message}`}</p>}

                        <FormLabel>Metoda organoleptyczna</FormLabel>
                        <Input type="text" disabled={isView}
                               placeholder="Metoda organoleptyczna" {...register("organolepticMethod", {required: true})} />
                        {errors.organolepticMethod && <p className="text-red-600">{`${errors.organolepticMethod.message}`}</p>}

                        <FormLabel>Grupa</FormLabel>
                        {/* Używamy SearchableDropdown z przekazanymi danymi */}
                        <SearchableDropdown
                            options={productGroupList}
                            selectedOption={selectedProductGroup?.id || null}
                            onSelect={(id) => setSelectedProductGroup(productGroupList.find(group => group.id === id) || null)}
                            disabled={isView}
                        />
                        {errors.group && <p className="text-red-600">{`${errors.group.message}`}</p>}


                        <h5 className="mt-6 mb-2 text-lg font-semibold">Oznaczenia</h5>

                        <div className="max-h-96 overflow-y-auto">
                            <table className="min-w-full table-auto border-collapse border border-gray-300">
                                <thead>
                                <tr className="bg-gray-100">
                                    <th className="p-2 border border-gray-300 text-left flex justify-content-between">
                                        <h6>Nazwa</h6>
                                        {!isView && (
                                            <div className="flex space-x-4 my-2">
                                                <StandardButton type={"button"}
                                                                onClick={() => setShowIndicationModal(true)}>
                                                    Dodaj nowy
                                                </StandardButton>
                                            </div>
                                        )}
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                {selectedIndicationList?.map((option, index) => (
                                    <tr key={option.id}
                                        className={`hover:bg-gray-100 ${index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}`}>
                                        <td className="p-2 border border-gray-300 flex justify-content-between">
                                            <p>{option.name}</p>
                                            {!isView && (
                                                <Button
                                                    variant="danger"
                                                    onClick={() => removeIndicationFromList(option.id)}
                                                    className="p-1"
                                                >
                                                    <FaTrashCan/>
                                                </Button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>


                    </Modal.Body>
                    <Modal.Footer>
                        {(isEdit || isAdd) && <StandardButton type="submit">Zapisz</StandardButton>}
                        <Button variant="secondary" onClick={handleCancel}>Anuluj</Button>
                    </Modal.Footer>
                </form>
            </FormProvider>

            <IndicationForAssortmentDictItem
                show={showIndicationModal}
                handleClose={() => setShowIndicationModal(false)}
                addNewIndication={addNewIndicationToList}
            />

        </Modal>
    );
};

export default AssortmentDictItem;

import React, {useContext, useEffect, useState} from "react";
import {FormProvider, useForm} from "react-hook-form";
import {AlertContext} from "../../../contexts/AlertsContext";
import {Button, Modal} from "react-bootstrap";
import {FormLabel} from "../../ui/Labels";
import {Input} from "../../ui/Input";
import {ModalSelection} from "../../ui/ModalSelection";
import {getAllIndications} from "../../../helpers/indicationApi";
import {addAssortment, updateAssortment} from "../../../helpers/assortmentApi";
import {getAllGroup} from "../../../helpers/groupApi";
import SearchableDropdown from "../../ui/SearchableDropdown";
import {Assortment, Indication, ProductGroup} from "../../../utils/types";

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

    const [indicationList, setIndicationList] = useState<Indication[]>([]);
    const [selectedIndication, setSelectedIndication] = useState<number[]>(item?.indications?.map(s => s.id) || []);
    const [selectedIndicationList, setSelectedIndicationList] = useState<Indication[]>(item?.indications || []);
    const [showIndicationModal, setShowIndicationModal] = useState(false);
    const [productGroupList, setProductGroupList] = useState<ProductGroup[]>([]);
    const [selectedProductGroup, setSelectedProductGroup] = useState<number | null>(item?.group?.id || null);

    // Używamy useEffect do ustawienia wartości formularza na podstawie przekazanego item
    useEffect(() => {
        if (item !== null) {
            reset(item); // Resetowanie formularza z danymi
            setSelectedIndication(item?.indications?.map(s => s.id) || []);
            setSelectedIndicationList(item?.indications || []);
            setSelectedProductGroup(item?.group?.id || null); // Ustawienie grupy produktów na podstawie item
        } else {
            resetForm();
        }
    }, [item]);

    const resetForm = () => {
        reset({id: '', name: '', group: ''});
        setSelectedIndication([]);
        setSelectedIndicationList([]);
        setSelectedProductGroup(null); // Resetowanie wybranej grupy produktów
    };

    const handleEdit = (formData: any) => {
        formData.indications = selectedIndication;
        formData.group = selectedProductGroup;
        try {
            updateAssortment(formData).then((response) => {
                if (response.status === 201 || response.status === 200) {
                    setAlertDetails({isAlert: true, message: "Edytowano definicję", type: "success"});
                    refresh();
                    handleClose();
                }
            });
        } catch (err) {
            setAlertDetails({isAlert: true, message: "Wystąpił błąd spróbuj ponownie później", type: "error"});
        }
    };

    const handleAdd = (formData: any) => {
        formData.indications = selectedIndication;
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
            setAlertDetails({isAlert: true, message: "Wystąpił błąd spróbuj ponownie później", type: "error"});
        }
    };

    const getIndications = () => {
        getAllIndications().then((res) => {
            if (res.status === 200) {
                setIndicationList(res.data);
            }
        });
    };

    useEffect(() => {
        getIndications();
    }, []);

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

    const handleSaveIndication = (selected: number[]) => {
        setSelectedIndication(selected);
        const selectedIndicationData = indicationList.filter(standard => selected.includes(standard.id));
        setSelectedIndicationList(selectedIndicationData);
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

                        <FormLabel>Grupa</FormLabel>
                        {/* Używamy SearchableDropdown z przekazanymi danymi */}
                        <SearchableDropdown
                            options={productGroupList}
                            selectedOption={selectedProductGroup}
                            onSelect={(id) => setSelectedProductGroup(id)}
                            disabled={isView}
                        />
                        {errors.group && <p className="text-red-600">{`${errors.group.message}`}</p>}


                        <h5 className="mt-6 mb-2 text-lg font-semibold">Wskazania</h5>
                        {!isView && (
                            <div className="flex space-x-4 my-2">
                                <Button variant="primary" onClick={() => setShowIndicationModal(true)}>
                                    Wybierz metody wspazania
                                </Button>
                            </div>
                        )}
                        <div className="max-h-96 overflow-y-auto">
                            <table className="min-w-full table-auto border-collapse border border-gray-300">
                                <thead>
                                <tr className="bg-gray-100">
                                    <th className="p-2 border border-gray-300 text-left">Nazwa</th>
                                </tr>
                                </thead>
                                <tbody>
                                {selectedIndicationList?.map((option, index) => (
                                    <tr key={option.id}
                                        className={`hover:bg-gray-100 ${index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}`}>
                                        <td className="p-2 border border-gray-300">{option.name}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>


                    </Modal.Body>
                    <Modal.Footer>
                        {(isEdit || isAdd) && <Button type="submit" variant="primary">Zapisz</Button>}
                        <Button variant="secondary" onClick={handleCancel}>Anuluj</Button>
                    </Modal.Footer>
                </form>
            </FormProvider>

            <ModalSelection
                title="Wybierz Standardy Próbkowania"
                options={indicationList}
                selectedOptions={selectedIndication}
                show={showIndicationModal}
                handleClose={() => setShowIndicationModal(false)}
                handleSave={handleSaveIndication}
            />
        </Modal>
    );
};

export default AssortmentDictItem;

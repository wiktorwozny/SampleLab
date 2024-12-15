import React, {useContext, useEffect, useState} from "react";
import {ProductGroup, SamplingStandards} from "../../../utils/types";
import {FormProvider, useForm} from "react-hook-form";
import {AlertContext} from "../../../contexts/AlertsContext";
import {Button, Modal} from "react-bootstrap";
import {FormLabel} from "../../ui/Labels";
import {Input} from "../../ui/Input";
import {getAllSamplingStandard} from "../../../helpers/samplingStandardApi";
import {ModalSelection} from "../../ui/ModalSelection";
import {addGroup, updateGroup} from "../../../helpers/groupApi";
import {StandardButton} from "../../ui/StandardButton";
import {handleApiError} from "../../../utils/handleApiError"; // Import modal component

interface ProductGroupDictItemProps {
    refresh: () => void;
    show: boolean;
    handleClose: () => void;
    item: ProductGroup | null;
    isView: boolean;
    isAdd: boolean;
    isEdit: boolean;
}

const ProductGroupDictItem: React.FC<ProductGroupDictItemProps> = ({
                                                                       refresh,
                                                                       show,
                                                                       handleClose,
                                                                       item,
                                                                       isView,
                                                                       isAdd,
                                                                       isEdit,
                                                                   }) => {

    const method = useForm();
    const {reset, handleSubmit, register, formState: {errors}} = method
    const {setAlertDetails} = useContext(AlertContext);

    const [samplingStandardsList, setSamplingStandardsList] = useState<SamplingStandards[]>([]);

    const [selectedSamplingStandards, setSelectedSamplingStandards] = useState<number[]>(item?.samplingStandards?.map(s => s.id) || []);

    const [selectedSamplingStandardsList, setSelectedSamplingStandardsList] = useState<SamplingStandards[]>(item?.samplingStandards || []);

    const [showSamplingStandardsModal, setShowSamplingStandardsModal] = useState(false);


    useEffect(() => {
        if (item !== null) {
            reset(item);
            setSelectedSamplingStandards(item?.samplingStandards?.map(s => s.id) || []);
            setSelectedSamplingStandardsList(item?.samplingStandards || []);
        } else {
            resetForm();
        }
    }, [item]);

    const resetForm = () => {
        reset({id: '', name: ''});
        setSelectedSamplingStandards([]);
        setSelectedSamplingStandardsList([]);
    };

    const handleEdit = (formData: any) => {
        formData.samplingStandards = selectedSamplingStandards;
        console.log()
        try {
            updateGroup(formData).then((response) => {
                if (response.status === 201 || response.status === 200) {
                    setAlertDetails({isAlert: true, message: "Edytowano definicję", type: "success"})
                    refresh();
                    handleClose();
                }
            }).catch((error) => {
                handleApiError(error, handleClose, setAlertDetails, "Nie udało się przetworzyć żądania.");
                refresh();
            });
        } catch (err) {
            setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"})
        }
    };

    const handleAdd = (formData: any) => {
        formData.samplingStandards = selectedSamplingStandards;
        console.log(formData)

        try {
            addGroup(formData).then((response) => {
                if (response.status === 201 || response.status === 200) {
                    setAlertDetails({isAlert: true, message: "Dodano nową definicję", type: "success"})
                    refresh();
                    handleClose();
                }
            }).catch((error) => {
                handleApiError(error, handleClose, setAlertDetails, "Nie udało się przetworzyć żądania.");
                refresh();
            });
        } catch (err) {
            setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"})
        }
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


    const handleSaveSamplingStandards = (selected: number[]) => {
        setSelectedSamplingStandards(selected);
        const selectedSamplingStandardsData = samplingStandardsList.filter(standard => selected.includes(standard.id));
        setSelectedSamplingStandardsList(selectedSamplingStandardsData);  // Update local list for display
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
                            <Input type="text" disabled={true} {...register("id", {
                                required: {
                                    value: false,
                                    message: "Pole wymagane"
                                }
                            })}
                            />
                            {errors.id && errors.id.message &&
                                <p className="text-red-600">{`${errors.id.message}`}</p>}
                        </div>
                        <FormLabel>Nazwa</FormLabel>
                        <Input type="text" disabled={isView}
                               placeholder="Nazwa" {...register("name", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            }
                        })}
                        />
                        {errors.name && errors.name.message &&
                            <p className="text-red-600">{`${errors.name.message}`}</p>}


                        {/* Buttons to open modal selection */}
                        {!isView && (
                            <div className="flex space-x-4 mt-4">
                                <StandardButton type={"button"} onClick={() => setShowSamplingStandardsModal(true)}>
                                    Wybierz normy pobrania
                                </StandardButton>
                            </div>
                        )}

                        <>
                            <h5 className="mt-6 mb-2 text-lg font-semibold">Normy pobrania</h5>
                            <div className="max-h-96 overflow-y-auto">
                                <table className="min-w-full table-auto border-collapse border border-gray-300">
                                    <thead>
                                    <tr className="bg-gray-100">
                                        <th className="p-2 border border-gray-300 text-left">Nazwa</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {selectedSamplingStandardsList?.map((option, index) => (
                                        <tr
                                            key={option.id}
                                            className={`hover:bg-gray-100 ${index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}`}
                                        >
                                            <td className="p-2 border border-gray-300">{option.name}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </>

                    </Modal.Body>
                    <Modal.Footer>
                        {(isEdit || isAdd) && (
                            <StandardButton type="submit">
                                Zapisz
                            </StandardButton>
                        )}
                        <Button variant="secondary" onClick={handleCancel}>
                            Anuluj
                        </Button>
                    </Modal.Footer>
                </form>
            </FormProvider>

            {/* Modal for selecting Sampling Standards */}
            <ModalSelection
                title="Wybierz Standardy Próbkowania"
                options={samplingStandardsList}
                selectedOptions={selectedSamplingStandards}
                show={showSamplingStandardsModal}
                handleClose={() => setShowSamplingStandardsModal(false)}
                handleSave={handleSaveSamplingStandards}
            />
        </Modal>
    );
};

export default ProductGroupDictItem;

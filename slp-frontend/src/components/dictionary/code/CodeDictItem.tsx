import React, {useContext, useEffect} from "react";
import {Code} from "../../../utils/types";
import {FormProvider, useForm} from "react-hook-form";
import {AlertContext} from "../../../contexts/AlertsContext";
import {addCode, updateCode} from "../../../helpers/codeApi";
import {Modal} from "react-bootstrap";
import {FormLabel} from "../../ui/Labels";
import {Input} from "../../ui/Input";
import {StandardButton} from "../../ui/StandardButton";
import {handleApiError} from "../../../utils/handleApiError";

interface CodeDictItemProps {
    refresh: () => void;
    show: boolean;
    handleClose: () => void;
    item: Code | null;
    isView: boolean;
    isAdd: boolean;
    isEdit: boolean;
}

const CodeDictItem: React.FC<CodeDictItemProps> = ({
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

    useEffect(() => {
        if (item !== null) {
            reset(item);
        } else {
            resetForm();
        }
    }, [item, reset]);

    const resetForm = () => {
        reset(
            {
                id: '',
                name: '',
            }
        );
    }

    const handleEdit = (formData: any) => {
        try {
            updateCode(formData).then((response) => {
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
        try {
            addCode(formData).then((response) => {
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
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"})
        }
    };

    const submit = (formData: any) => {
        if (isEdit) {
            handleEdit(formData);
        } else {
            handleAdd(formData);
        }
        resetForm();
    }

    const handleCancel = () => {
        handleClose();
        resetForm()
    }

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

                        <FormLabel>Kod</FormLabel>
                        <Input maxLength={2} minLength={2} placeholder="Kod" type="text" {...register("id", {

                            required: {
                                value: false,
                                message: "Pole wymagane"
                            }
                        })}
                        />
                        {errors.id && errors.id.message &&
                            <p className="text-red-600">{`${errors.id.message}`}</p>}

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
                    </Modal.Body>
                    <Modal.Footer>
                        {(isEdit || isAdd) && (
                            <StandardButton type={"submit"}
                                            className="bg-blue-600 text-white font-semibold py-2 px-4 rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
                                Zapisz
                            </StandardButton>
                        )}
                        <StandardButton type={"button"}
                                        className="bg-gray-600 text-white font-semibold py-2 px-4 rounded hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-opacity-50"
                                        onClick={handleCancel}>
                            Anuluj
                        </StandardButton>
                    </Modal.Footer>
                </form>
            </FormProvider>
        </Modal>
    );
}

export default CodeDictItem;
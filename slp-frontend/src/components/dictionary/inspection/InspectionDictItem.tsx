import React, {useContext, useEffect} from "react";
import {Inspection} from "../../../utils/types";
import {FormProvider, useForm} from "react-hook-form";
import {AlertContext} from "../../../contexts/AlertsContext";
import {Button, Modal} from "react-bootstrap";
import {FormLabel} from "../../ui/Labels";
import {Input} from "../../ui/Input";
import {addInspection, updateInspection} from "../../../helpers/inspectionApi";

interface InspectionDictItemProps {
    refresh: () => void;
    show: boolean;
    handleClose: () => void;
    item: Inspection | null;
    isView: boolean;
    isAdd: boolean;
    isEdit: boolean;
}

const InspectionDictItem: React.FC<InspectionDictItemProps> = ({
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
            updateInspection(formData).then((response) => {
                if (response.status === 201 || response.status === 200) {
                    setAlertDetails({isAlert: true, message: "Edytowano definicję", type: "success"})
                    refresh();
                    handleClose();
                }
            })
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
        }
    };

    const handleAdd = (formData: any) => {
        try {
            addInspection(formData).then((response) => {
                if (response.status === 201 || response.status === 200) {
                    setAlertDetails({isAlert: true, message: "Dodano nową definicję", type: "success"})
                    refresh();
                    handleClose();
                }
            })
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
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
                    </Modal.Body>
                    <Modal.Footer>
                        {(isEdit || isAdd) && (
                            <Button type={"submit"} variant="primary">
                                Zapisz
                            </Button>
                        )}
                        <Button variant="secondary" onClick={handleCancel}>
                            Anuluj
                        </Button>
                    </Modal.Footer>
                </form>
            </FormProvider>
        </Modal>
    );
}

export default InspectionDictItem;
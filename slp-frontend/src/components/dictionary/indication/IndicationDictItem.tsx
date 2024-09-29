import React, {useContext, useEffect} from "react";
import {FormProvider, useForm} from "react-hook-form";
import {AlertContext} from "../../../contexts/AlertsContext";
import {Indication} from "../../../utils/types";
import {addIndication, updateIndication} from "../../../helpers/indicationApi";
import {Button, Modal} from "react-bootstrap";
import {FormLabel} from "../../ui/Labels";
import {Input} from "../../ui/Input";

interface IndicationDictItemProps {
    refresh: () => void;
    show: boolean;
    handleClose: () => void;
    item: Indication | null;
    isView: boolean;
    isAdd: boolean;
    isEdit: boolean;
}

const IndicationDictItem: React.FC<IndicationDictItemProps> = ({
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

    const unitPattern = /^\d*(\.\d+)?\s?(g|gram|grams|kg|kilogram|kilograms|mg|milligram|milligrams|oz|ounce|ounces|lb|pound|pounds|ml|milliliter|milliliters|l|liter|liters|fl\s?oz|fluid\s?ounce|fluid\s?ounces|gal|gallon|gallons)$/;

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
                norm: '',
                unit: '',
                laboratory: '',
            }
        );

    }
    const handleEdit = (formData: any) => {
        try {
            updateIndication(formData).then((response) => {
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
            addIndication(formData).then((response) => {
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
                        <FormLabel>Norma</FormLabel>
                        <Input type="text" disabled={isView}
                               placeholder="Norma" {...register("norm", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            }
                        })}
                        />
                        {errors.norm && errors.norm.message &&
                            <p className="text-red-600">{`${errors.norm.message}`}</p>}

                        <FormLabel>Jednostka</FormLabel>
                        <Input
                            type="text"
                            disabled={isView}
                            placeholder="Jednostka"
                            {...register("unit", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane"
                                },
                                pattern: {
                                    value: unitPattern,
                                    message: "Niepoprawny format jednostki",
                                },
                            })}
                        />
                        {errors.unit && errors.unit.message &&
                            <p className="text-red-600">{`${errors.unit.message}`}</p>}

                        <FormLabel>Kod laboratorium</FormLabel>
                        <Input type="text" disabled={isView}
                               maxLength={3}
                               minLength={3}
                               placeholder="Kod laboratorium" {...register("laboratory", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            }
                        })}
                        />
                        {errors.laboratory && errors.laboratory.message &&
                            <p className="text-red-600">{`${errors.laboratory.message}`}</p>}


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

};

export default IndicationDictItem;
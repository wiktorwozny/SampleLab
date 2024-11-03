import React, {useEffect} from "react";
import {FormProvider, useForm} from "react-hook-form";
import {Indication} from "../../../utils/types";
import {Modal} from "react-bootstrap";
import {FormLabel} from "../../ui/Labels";
import {Input} from "../../ui/Input";
import {StandardButton} from "../../ui/StandardButton";

interface IndicationDictItemProps {
    show: boolean;
    handleClose: () => void;
    addNewIndication: (newIndication: Indication) => void; // Prop do przekazywania nowego obiektu
}

const IndicationForAssortmentDictItem: React.FC<IndicationDictItemProps> = ({
                                                                                show,
                                                                                handleClose,
                                                                                addNewIndication, // Odbieramy funkcję rodzica
                                                                            }) => {
    const method = useForm();
    const {reset, handleSubmit, register, formState: {errors}} = method;

    useEffect(() => {
        resetForm();
    }, [reset]);

    const resetForm = () => {
        reset({
            id: '',
            name: '',
            method: '',
            unit: '',
            laboratory: '',
        });
    };

    const submit = (formData: any) => {
        const newIndication: Indication = {
            id: Date.now(),
            name: formData.name,
            method: formData.method,
            unit: formData.unit,
            laboratory: formData.laboratory,
        };

        addNewIndication(newIndication);
        handleClose();
        resetForm();
    };

    const handleCancel = () => {
        handleClose();
        resetForm();
    };

    return (
        <Modal show={show} onHide={handleClose}>
            <FormProvider {...method}>
                <form className="bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
                    <Modal.Header closeButton>
                        <Modal.Title>{'Nowy'}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <FormLabel>Nazwa</FormLabel>
                        <Input
                            type="text"
                            placeholder="Nazwa"
                            {...register("name", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane",
                                },
                            })}
                        />
                        {errors.name && errors.name.message && (
                            <p className="text-red-600">{`${errors.name.message}`}</p>
                        )}

                        <FormLabel>Metoda</FormLabel>
                        <Input
                            type="text"
                            placeholder="Metoda"
                            {...register("method", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane",
                                },
                            })}
                        />
                        {errors.method && errors.method.message && (
                            <p className="text-red-600">{`${errors.method.message}`}</p>
                        )}

                        <FormLabel>Jednostka</FormLabel>
                        <Input
                            type="text"
                            placeholder="Jednostka"
                            {...register("unit", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane",
                                },
                            })}
                        />
                        {errors.unit && errors.unit.message && (
                            <p className="text-red-600">{`${errors.unit.message}`}</p>
                        )}

                        <FormLabel>Kod laboratorium</FormLabel>
                        <Input
                            type="text"
                            maxLength={3}
                            minLength={3}
                            placeholder="Kod laboratorium"
                            {...register("laboratory", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane",
                                },
                            })}
                        />
                        {errors.laboratory && errors.laboratory.message && (
                            <p className="text-red-600">{`${errors.laboratory.message}`}</p>
                        )}
                    </Modal.Body>
                    <Modal.Footer>
                        <StandardButton
                            type={"submit"}
                            className="bg-blue-600 text-white font-semibold py-2 px-4 rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                        >
                            Zapisz
                        </StandardButton>
                        <StandardButton
                            type={"reset"}
                            className="bg-gray-600 text-white font-semibold py-2 px-4 rounded hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-opacity-50"
                            onClick={handleCancel}
                        >
                            Anuluj
                        </StandardButton>
                    </Modal.Footer>
                </form>
            </FormProvider>
        </Modal>
    );
};

export default IndicationForAssortmentDictItem;
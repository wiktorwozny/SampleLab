import React, {useContext, useEffect} from "react";
import {useForm} from "react-hook-form";
import {AlertContext} from "../../../contexts/AlertsContext";
import {Indication} from "../../../utils/types";

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
    const {reset, handleSubmit, register, formState: {errors}, setValue} = method
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
                wijharsCode: '',
                address: null,
            }
        );

    }

    return (
        <div></div>
    );

};

export default IndicationDictItem;
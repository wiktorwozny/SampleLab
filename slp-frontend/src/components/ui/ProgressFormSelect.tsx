import React, {useContext} from 'react';
import Select, {Props as SelectProps} from 'react-select';
import {Controller, FieldValues, useFormContext} from 'react-hook-form';
import {ProgressState} from "../../utils/types";
import {updateStatus} from "../../helpers/sampleApi";
import {AlertContext} from "../../contexts/AlertsContext";
import {ControllerRenderProps} from "react-hook-form/dist/types/controller";

interface CustomSelectProps extends Omit<SelectProps, 'options'> {
    className?: string;
    options: { value: any; label: string; target: { name: string } }[];
}

export const ProgressFormSelect = (
    ({className = '', options, name, onChange, onBlur, defaultValue, sample, ...props}: any) => {
        const {control} = useFormContext();
        const {setAlertDetails} = useContext(AlertContext);

        const onChangeHandler = async (field: ControllerRenderProps<FieldValues, any>, sampleId: number, option: ProgressState) => {
            try {
                console.log(option.value);
                let response = await updateStatus(sampleId, String(option.value))
                if (response.status === 200 || response.status === 201) {
                    field.onChange(option.value);
                }
            } catch (err) {
                console.log(err)
                setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
            }

        }

        return (
            <Controller
                name={name}
                control={control}
                render={({field}) => (
                    <Select
                        className={`mb-2 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline ${className}`}
                        options={options}
                        classNamePrefix="select"
                        isSearchable={true}
                        onBlur={field.onBlur}
                        defaultValue={defaultValue}
                        onChange={(selectedOption: any) => {
                            onChangeHandler(field, sample.id, selectedOption);
                        }}
                        {...props}
                    />
                )}
            />
        );
    }
);

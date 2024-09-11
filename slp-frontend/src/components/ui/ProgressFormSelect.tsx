import React, {useContext} from 'react';
import Select, {Props as SelectProps} from 'react-select';
import {Controller, FieldValues, useFormContext} from 'react-hook-form';
import {ProgressState} from "../../utils/types";
import {updateStatus} from "../../helpers/sampleApi";
import {AlertContext} from "../../contexts/AlertsContext";
import {ControllerRenderProps} from "react-hook-form/dist/types/controller";
import { SummarySample } from '../../utils/types';
interface CustomSelectProps extends Omit<SelectProps, 'options'> {
    className?: string;
    options: { value: any; label: string; target: { name: string } }[];
}

export const ProgressFormSelect = (
    ({className = '', options, name, onChange, onBlur, defaultValue, sample, setSamples, ...props}: any) => {
        const {control} = useFormContext();
        const {setAlertDetails} = useContext(AlertContext);

        const onChangeHandler = async (field: ControllerRenderProps<FieldValues, any>, sampleId: number, option: ProgressState) => {
            try {
                console.log(option.value);
                let response = await updateStatus(sampleId, String(option.value))
                if (response.status === 200 || response.status === 201) {
                    setSamples((prev: SummarySample[]) => {
                        // Znajdź indeks elementu, który chcesz zmienić
                        const index = prev.findIndex(s => s.id === sampleId);
                        
                        // Jeśli element został znaleziony
                        if (index !== -1) {
                          // Tworzymy nową tablicę z elementem zaktualizowanym na odpowiednim miejscu
                          const updatedSamples = [
                            ...prev.slice(0, index),
                            { ...prev[index], progressStatus: option.value },
                            ...prev.slice(index + 1)
                          ];
                          
                          return updatedSamples;
                        }
                      
                        // Jeśli element nie został znaleziony, zwracamy oryginalną tablicę
                        return prev;
                      });
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

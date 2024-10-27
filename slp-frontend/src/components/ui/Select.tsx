import React, { useEffect, useRef } from 'react';
import Select, {Props as SelectProps} from 'react-select';
import {Controller, useFormContext} from 'react-hook-form';

interface CustomSelectProps extends Omit<SelectProps, 'options'> {
    className?: string;
    options: { value: any; label: string; target: { name: string } }[];
    chosenGroup: string;
}

export const FormSelect = (
    ({className = '', options, name, onChange, onBlur, isDisabled = false, chosenGroup = '', ...props}: any) => {
        const {control} = useFormContext();
        const selectRef:any = useRef();
        useEffect(()=>{
            selectRef.current?.clearValue();
        },[chosenGroup])
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
                        onChange={(selectedOption: any) => {
                            field.onChange(selectedOption? selectedOption.value: null);
                        }}
                        isDisabled={isDisabled}
                        ref={selectRef}
                        {...props}
                    />
                )}
            />
        );
    }
);

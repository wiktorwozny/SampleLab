import React, { forwardRef } from 'react';
import Select, { Props as SelectProps } from 'react-select';
import { Controller, useFormContext } from 'react-hook-form';

interface CustomSelectProps extends Omit<SelectProps, 'options'> {
    className?: string;
    options: { value: any; label: string; target:{name:string} }[];
}

export const FormSelect = (
    ({ className = '', options, name, onChange, onBlur, ...props}:any) => {
        // console.log(props)
        // return (
        //     <Select
        //         className={`mb-2 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline ${className}`}
        //         options={options}
        //         classNamePrefix="select"
        //         isSearchable={true}
        //         name={name}
        //         onBlur={onBlur}
        //         onChange={onChange}
        //         {...props}
        //     />
        // );
        const { control } = useFormContext();
        return (
            <Controller
                name={name}
                control={control}
                render={({ field }) => (
                    <Select
                        className={`mb-2 shadow appearance-none border rounded w-full text-gray-700 leading-tight focus:outline-none focus:shadow-outline ${className}`}
                        options={options}
                        classNamePrefix="select"
                        isSearchable={true}
                        onBlur={(e) => {
                            onBlur(e)
                        }}
                        onChange={(selectedOption:{value:any, label:string}) => {
                            console.log(selectedOption)
                            console.log(field)
                            field.onChange(selectedOption?.value);
                        }}
                        {...props}
                    />
                )}
            />
        );
    }
);

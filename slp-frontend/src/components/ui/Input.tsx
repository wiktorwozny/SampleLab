import * as React from 'react';

export interface InputProps
    extends React.InputHTMLAttributes<HTMLInputElement> {
    className?: string;
    maxLength?: number;
}

export interface TextAreaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {

}

export const Input = React.forwardRef<HTMLInputElement, InputProps>(
    ({
         className = '',
         name,
         type,
         placeholder,
         value,
         disabled,
         onChange,
         onClick,
         maxLength,
         ...props
     }, ref) => {
        return (
            <input
                className={'mb-2 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline form-control' + className}
                type={type}
                ref={ref}
                name={name}
                placeholder={placeholder}
                value={value}
                disabled={disabled}
                onChange={onChange}
                onClick={onClick}
                maxLength={maxLength}
                {...props}/>
        )
    }
);


export const BigTextInput = React.forwardRef<HTMLTextAreaElement, TextAreaProps>(
    ({
         className = '',
         name,
         placeholder,
         value,
         disabled,
         maxLength,
         rows,
         ...props
     }, ref) => {
        return (
            <textarea
                className={'mb-2 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline form-control' + className}
                ref={ref}
                name={name}
                placeholder={placeholder}
                value={value}
                disabled={disabled}
                maxLength={maxLength}
                rows={rows}
                {...props}
            />
        )
    }
);

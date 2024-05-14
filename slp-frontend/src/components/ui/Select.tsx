import React, { SelectHTMLAttributes, forwardRef } from 'react';

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
    className?: string;
    options: { value: any; label: string }[];
}

export const Select = forwardRef<HTMLSelectElement, SelectProps>(
    ({ className = '', options, ...props }, ref) => {
        return (
            <select
                className={`mb-2 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline ${className}`}
                ref={ref}
                {...props}
            >
                {options.map((option, index) => (
                    <option 
                        key={option.value} 
                        value={option.value} 
                        className='py-2 px-4 border-0 leading-tight rounded shadow appearance-none'
                        {...(index === 0 ? { defaultValue: option.value } : {})}
                    >
                        {option.label}
                    </option>
                ))}
            </select>
        );
    }
);
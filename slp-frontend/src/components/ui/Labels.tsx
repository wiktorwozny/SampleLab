import * as React from 'react';

export interface LabelProps
    extends React.InputHTMLAttributes<HTMLLabelElement> {
        className?: string;
}

export const FormLabel = ({ className="" ,children , ...props}:React.HTMLAttributes<HTMLLabelElement>) => {
        return(<label 
                    {...props}
                    className={`form-label text-mb ${className}`}
                >
                {children}
        </label>)
    }
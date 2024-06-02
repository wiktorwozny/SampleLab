import React, { useState, forwardRef } from 'react';
import { Controller, useFormContext, ChangeHandler } from 'react-hook-form';
import { Button } from './Button'; // Make sure this import is correct
import AddressForm from '../AddressForm'; // Make sure this import is correct

interface AddressValueType {
  city: string;
  street: string;
  zipCode: string;
}

interface AddressControllerProps {
  className?: string;
  name: string;
  onChange: ChangeHandler;
  onBlur: ChangeHandler;
}

export const AddressController = forwardRef<HTMLDivElement, AddressControllerProps>(
  ({ className = '', name, onChange, onBlur, ...props }, ref) => {
    const { control } = useFormContext();
    const [isOpen, setIsOpen] = useState<boolean>(false);

    return (
      <Controller
        name={name}
        control={control}
        render={({ field }) => (
          <>
            <div className={"flex mb-2 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight justify-between " + className} ref={ref}>
              {field.value&&<div>ul. {field.value.street}, {field.value.zipCode} {field.value.city}</div>}
              {!field.value&&<div className='leading-9'>Wprowadź adres</div>}
              <button className="bg-slate-700 rounded text-white p-2 hover:bg-slate-600" onClick={(e) => {e.preventDefault();setIsOpen(true)}}>
                zmień
              </button>
            </div>
            <AddressForm value={field.onChange} setIsOpen={setIsOpen} onBlur={onBlur} isOpen={isOpen}/>
          </>
        )}
      />
    );
  }
);


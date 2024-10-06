import React, {forwardRef, useEffect, useState} from 'react';
import {ChangeHandler, Controller, useFormContext} from 'react-hook-form';
import AddressForm from '../AddressForm';
import {Address} from "../../utils/types";
import {Button} from "react-bootstrap"; // Make sure this import is correct


interface AddressControllerProps {
    item?: Address | null
    className?: string;
    name: string;
    onChange: ChangeHandler;
    onBlur: ChangeHandler;
    isDisabled?: boolean;
}

export const AddressController = forwardRef<HTMLDivElement, AddressControllerProps>(
    ({className = '', name, onChange, item = null, onBlur, isDisabled = false, ...props}, ref) => {
        const {control} = useFormContext();
        const [isOpen, setIsOpen] = useState<boolean>(false);

        useEffect(() => {
            console.log(item);
        }, [item]);

        return (
            <Controller
                name={name}
                control={control}
                render={({field}) => (
                    <>
                        <div
                            className={"flex mb-2 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight justify-between " + className}
                            ref={ref}>
                            {field.value && <div className='flex flex-col'>
                                <div>
                                    ul. {field.value.street}
                                </div>
                                <div>
                                    {field.value.zipCode} {field.value.city}
                                </div>
                            </div>}
                            {!field.value && <div className='leading-9'>Wprowadź adres</div>}
                            <Button
                                variant={"secondary"}
                                disabled={isDisabled}
                                className={"bg-slate-700 rounded text-white p-2 hover:bg-slate-600"}
                                onClick={(e) => {
                                    e.preventDefault();
                                    setIsOpen(true)
                                }}>
                                zmień
                            </Button>
                        </div>
                        <AddressForm item={item} value={field.onChange} setIsOpen={setIsOpen} onBlur={onBlur}
                                     isOpen={isOpen}/>
                    </>
                )}
            />
        );
    }
);


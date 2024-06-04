import React, {FC} from "react";
import { useForm, ChangeHandler } from "react-hook-form";
import { FormLabel } from "./ui/Labels";
import { Input } from "./ui/Input";
import { Button } from "./ui/Button";
import { BiChevronLeft } from "react-icons/bi";
const AddressForm: FC<{value: any, setIsOpen: (isOpen: boolean) => void, onBlur: ChangeHandler, isOpen:boolean}> = ({value, setIsOpen, onBlur,isOpen}) => {
    const { handleSubmit, register, formState: { errors }} = useForm();

    const submit = (values:any) => {
        console.log(values)
        value(values)
        setIsOpen(false)
    }

    return(<div className={(isOpen? "min-w-full min-h-full max-w-full max-h-full":"max-w-0 max-h-0 min-w-0 min-h-0")+" flex items-center justify-center fixed z-2 top-0 left-0 overflow-hidden transition"}>
        <div className="bg-slate-300 opacity-85 absolute z-1 w-full h-full"></div>
        <form className="z-2 bg-white p-5 rounded w-1/3 relative flex items-center flex-col">
            <BiChevronLeft className="absolute left-0 top-0 text-4xl" onClick={()=>{setIsOpen(false)}}/>
            <h2 className="font-bold text-2xl mb-2">Formularz adresowy</h2>
            <div className="w-3/4">
                <FormLabel className="w-full text-start">Miejscowość</FormLabel>
                <Input
                    {...register("city",{
                        required:{
                        value:true,
                        message:"Pole wymagane"
                    }})}
                />
                {errors.city && errors.city.message &&
                        <p className="text-red-600 w-full text-start">{`${errors.city.message}`}</p>}

                <FormLabel className="w-full text-start">Ulica</FormLabel>
                <Input
                    {...register("street",{
                        required:{
                        value:true,
                        message:"Pole wymagane"
                    }})}
                />
                {errors.street && errors.street.message &&
                        <p className="text-red-600 w-full text-start">{`${errors.street.message}`}</p>}

                <FormLabel className="w-full text-start">Kod pocztowy</FormLabel>
                <Input
                    {...register("zipCode",{
                        required:{
                        value:true,
                        message:"Pole wymagane"
                    },
                    pattern:{
                        value:/^\d{2}-\d{3}$/,
                        message:"Zły format kodu pocztowego"
                    }})}
                />
                {errors.zipCode && errors.zipCode.message &&
                        <p className="text-red-600 w-full text-start">{`${errors.zipCode.message}`}</p>}
            </div>
            <div className="flex justify-end mt-2 w-full">
                <Button type="button" className="relative right-0" onBlur={onBlur} onClick={(e)=>{e.preventDefault();handleSubmit(submit)()}}>zapisz</Button>
            </div>
        </form>
    </div>)
}


export default AddressForm;
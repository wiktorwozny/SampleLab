import { FC, useEffect, useState, useRef } from 'react'
import { Input } from './ui/Input';
import { Select } from './ui/Select';
import { useForm } from 'react-hook-form';
import { FormLabel } from './ui/Labels';
import { Button } from './ui/Button';
import { Address } from '../utils/types';
import { addSample } from '../helpers/samplingApi';
import { getAllAddresses } from '../helpers/addressApi';
const ReportDataForm:FC<{}>=()=>{

    const { handleSubmit, register, formState: { errors } } = useForm();
    const [message, setMessage] = useState<String>("")
    const [addresses, setAddresses] = useState<Address []>([])

    useEffect(() => {
        const getAddresses = async() => {
            try {
                let response = await getAllAddresses()
                if(response.status === 200){
                    setAddresses(response.data)
                }
            }catch(err) {
                console.log(err)
            }
        }

        getAddresses()
    },[])

    const submit = async(values:any) => {
        console.log(values)
        
        try{
            let response = await addSample(values)
            console.log(response)
        }catch(err){
            console.log(err)
        }
    }

    return(<div className='flex flex-col justify-center items-center'>
        <form className="lg:w-1/3 md:w-1/2 w-3/4 flex justify-center flex-col p-5 bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
            <h2 className="text-center font-bold mb-3 text-2xl">Formularz dodawnia dodatkowych informacji</h2>
            <FormLabel>Nazwa manofaktury</FormLabel>
            <Input {...register("manufacturerName",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            {errors.manufacturerName && errors.manufacturerName.message&&<p className="text-red-600">{`${errors.manufacturerName.message}`}</p>}
            
            <FormLabel>Adres manofaktury</FormLabel>
            <Select
                className="my-custom-class"
                options={addresses.map(address=>({value: JSON.stringify(address), label:`${address.street} ${address.city}`}))}
                {...register("manufacturerName",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />
            
            <FormLabel>Nazwa dostawcy</FormLabel>
            <Input {...register("supplierName",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Adres dostawcy</FormLabel>
            <Select
                className="my-custom-class"
                options={addresses.map(address=>({value: JSON.stringify(address), label:`${address.street} ${address.city}`}))}
                {...register("supplierAddress",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Nazwa sprzedawcy</FormLabel>
            <Input {...register("sellerName",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Adres sprzedawcy</FormLabel>
            <Select
                className="my-custom-class"
                options={addresses.map(address=>({value: JSON.stringify(address), label:`${address.street} ${address.city}`}))}
                {...register("sellerAddress",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Nazwa odbiorcy</FormLabel>
            <Input {...register("recipientName",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Adres odbiorcy</FormLabel>
            <Select
                className="my-custom-class"
                options={addresses.map(address=>({value: JSON.stringify(address), label:`${address.street} ${address.city}`}))}
                {...register("recipientAddress",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Numer pracy</FormLabel>
            <Input type='number' {...register("jobNumber",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />
            
            <FormLabel>Mechanizm</FormLabel>
            <Input {...register("mechanism",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Metoda dostawcy</FormLabel>
            <Input {...register("deliveryMethod",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <Button type="submit" className='mt-3'>Dodaj dodatkowe informacje do raportu</Button>
        </form>
    </div>)
}

export default ReportDataForm;
import {FC, useEffect, useState} from 'react'
import {Input} from './ui/Input';
import {FormSelect} from './ui/Select';
import {FormProvider, useForm} from 'react-hook-form';
import {FormLabel} from './ui/Labels';
import {Button} from './ui/Button';
import {Address, ReportData} from '../utils/types';
import {getAllAddresses} from '../helpers/addressApi';
// import { addReportDataToSample } from '../helpers/sampleApi';
// import {addReportDataToSample} from '../helpers/reportDataApi';
import { addReportData } from '../helpers/reportDataApi';
import {useNavigate, useParams} from 'react-router-dom';
import AddressForm from './AddressForm';
import { AddressController } from './ui/AddressController';
import { getReportDataBySampleId } from '../helpers/reportDataApi';

type ReportDataFormFields = {
    manufacturerName: string,
    manufacturerAddress: Address,
    supplierName: string,
    supplierAddress: Address,
    sellerName: string,
    sellerAddress: Address,
    recipientName: string,
    recipientAddress: Address,
    jobNumber: number,
    mechanism: string,
    deliveryMethod: string
}

const ReportDataForm: FC<{}> = ({}) => {
    const [reportData, setReportData] = useState<ReportData | null>(null);
    const method = useForm({
        defaultValues: {
            manufacturerName: reportData?.manufacturerName || '',
            manufacturerAddress: reportData?.manufacturerAddress || null,
            supplierName: reportData?.supplierName || '',
            supplierAddress: reportData?.supplierAddress || null,
            sellerName: reportData?.sellerName || '',
            sellerAddress: reportData?.sellerAddress || null,
            recipientName: reportData?.recipientName || "",
            recipientAddress: reportData?.recipientAddress || null,
            jobNumber: reportData?.jobNumber || null,
            mechanism: reportData?.mechanism || '',
            deliveryMethod: reportData?.deliveryMethod || '',
        },
    });
    const {handleSubmit, register, formState: {errors}, setValue} = method;
    const [message, setMessage] = useState<String>("")
    const [addresses, setAddresses] = useState<Address []>([])
    const [isSeller, setIsSeller] = useState<Boolean>(true)
    const {sampleId} = useParams()
    const navigate = useNavigate()
    useEffect(() => {
        const getAddresses = async () => {
            try {
                let response = await getAllAddresses()
                if (response.status === 200) {
                    setAddresses(response.data)
                }
            } catch (err) {
                console.log(err)
            }
        }
        const getReportData = async () => {
            try {
                let response = await getReportDataBySampleId(sampleId);
                if (response.status === 200) {
                    setReportData(response.data)
                }
            } catch (err) {
                console.log(err)
            }
        }
        if(sampleId){
            getReportData();
        }
    }, [])

    const fieldsToSet: Array<keyof ReportDataFormFields> = [
        'manufacturerName',
        'manufacturerAddress',
        'supplierName',
        'supplierAddress',
        'sellerName',
        'sellerAddress',
        'recipientName',
        'recipientAddress',
        'jobNumber',
        'mechanism',
        'deliveryMethod'
    ]

    useEffect(() => {
        fieldsToSet.forEach(field => {
            if (reportData && reportData[field]) {
                setValue(field, reportData[field]);
            }
        });
    }, [reportData, setValue]);

    const submit = async (values: any) => { 
        values.sampleId = sampleId===undefined? null:parseInt(sampleId) 
        if(reportData){
            values.id = reportData.id;
        }
        console.log(values)
        try {
            let response = await addReportData(values)
            console.log(response)
            navigate(`/sample/${sampleId}`);
        } catch (err) {
            console.log(err)
        }
    }

    return (<div className='flex flex-col justify-center items-center w-full'>
        <h2 className="text-center font-bold my-10 text-2xl">Dodawanie dodatkowych informacji</h2>
        <FormProvider {...method}>
            <form className="w-3/5 flex justify-between p-5 bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
                <div className='w-1/3'>
                    {/* <h2 className='text-2xl font-bold'></h2> */}
                    <FormLabel>Nazwa producenta</FormLabel>
                    <Input {...register("manufacturerName", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />
                    {errors.manufacturerName && errors.manufacturerName.message &&
                        <p className="text-red-600">{`${errors.manufacturerName.message}`}</p>}

                    <FormLabel>Adres producenta</FormLabel>
                    <AddressController
                        className="my-custom-class"
                        {...register("manufacturerAddress", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            }
                        })}
                    />
                    {errors.manufacturerAddress && errors.manufacturerAddress.message &&
                        <p className="text-red-600">{`${errors.manufacturerAddress.message}`}</p>}

                    <div className='flex'>
                        <div className='flex mr-7'>
                            <input className="form-check-input" type="radio" name="typeOfAddress" onChange={val=>{setIsSeller(true)}} defaultChecked/>
                            <div>&nbsp;sprzedawca</div>
                        </div>
                        <div className='flex'>
                            <input className="form-check-input" type="radio" name="typeOfAddress" onChange={val=>{setIsSeller(false)}}/>
                            <div>&nbsp;dostawca</div>
                        </div>
                    </div>

                    {!isSeller&&<>
                    <FormLabel>Nazwa dostawcy</FormLabel>
                    <Input {...register("supplierName", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />
                    {errors.supplierName && errors.supplierName.message &&
                        <p className="text-red-600">{`${errors.supplierName.message}`}</p>}

                    <FormLabel>Adres dostawcy</FormLabel>
                    <AddressController
                        className="my-custom-class"
                        {...register("supplierAddress", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            }
                        })}
                    />
                    {errors.supplierAddress && errors.supplierAddress.message &&
                        <p className="text-red-600">{`${errors.supplierAddress.message}`}</p>}
                    </>}
                    

                    {isSeller&&<><FormLabel>Nazwa sprzedawcy</FormLabel>
                    <Input {...register("sellerName", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />
                    {errors.sellerName && errors.sellerName.message &&
                        <p className="text-red-600">{`${errors.sellerName.message}`}</p>}

                    <FormLabel>Adres sprzedawcy</FormLabel>
                    <AddressController
                        className="my-custom-class"
                        {...register("sellerAddress", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            }
                        })}
                    />
                    {errors.sellerAddress && errors.sellerAddress.message &&
                        <p className="text-red-600">{`${errors.sellerAddress.message}`}</p>}
                    </>}

                    <FormLabel>Nazwa odbiorcy</FormLabel>
                    <Input {...register("recipientName", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />
                    {errors.recipientName && errors.recipientName.message &&
                        <p className="text-red-600">{`${errors.recipientName.message}`}</p>}

                    <FormLabel>Adres odbiorcy</FormLabel>
                    <AddressController
                        className="my-custom-class"
                        {...register("recipientAddress", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            }
                        })}
                    />
                    {errors.recipientAddress && errors.recipientAddress.message &&
                        <p className="text-red-600">{`${errors.recipientAddress.message}`}</p>}
                </div>

                <div className='w-1/3 flex flex-col'>
                    <h2 className='text-2xl font-bold'>Dane Klienta</h2>
                    <FormLabel>Numer zlecenia</FormLabel>
                    <Input type='number' {...register("jobNumber", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />
                    {errors.jobNumber && errors.jobNumber.message &&
                        <p className="text-red-600">{`${errors.jobNumber.message}`}</p>}
                    <FormLabel>Mechanizm</FormLabel>
                    <Input {...register("mechanism", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />
                    {errors.mechanism && errors.mechanism.message &&
                        <p className="text-red-600">{`${errors.mechanism.message}`}</p>}

                    <FormLabel>Metoda dostawcy</FormLabel>
                    <Input {...register("deliveryMethod", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />
                    {errors.deliveryMethod && errors.deliveryMethod.message &&
                        <p className="text-red-600">{`${errors.deliveryMethod.message}`}</p>}

                    <Button type="submit" className='mt-3 w-full justify-self-end' onClick={()=>{handleSubmit(submit)()}}>Dodaj</Button>
                </div>
            </form>
        </FormProvider>
    </div>)
}

export default ReportDataForm;
import { FC, useEffect, useState, useRef } from 'react'
import { Input } from './ui/Input';
import { FormSelect } from './ui/Select';
import { FormProvider, useForm } from 'react-hook-form';
import { FormLabel } from './ui/Labels';
import {Button, CancelButton} from './ui/Button';
import { getAllCodes } from '../helpers/codeApi';
import { getAllClients } from '../helpers/clientApi';
import { getAllInspection } from '../helpers/inspectionApi';
import { getAllGroup } from '../helpers/groupApi';
import { getAllSamplingStandard } from '../helpers/samplingStandardApi';
import { getAllReportData } from '../helpers/reportDataApi';
import { ListFormat } from 'typescript';
import { Code, Client, Inspection, ProductGroup, SamplingStandards, ReportData, Sample } from '../utils/types';
import { addSample } from '../helpers/samplingApi';
import { useNavigate } from 'react-router-dom';
import { useContext } from 'react';
import { AlertContext } from '../contexts/AlertsContext';
const SampleForm:FC<{}>=()=>{

    const method = useForm();
    const { handleSubmit, register, formState: { errors }} = method
    const [message, setMessage] = useState<String>("")
    const [codes, setCodes] = useState<Code []>([])
    const [clients, setClients] = useState<Client []>([])
    const [inspections, setInspections] = useState<Inspection []>([])
    const [productGroup, setProductGroup] = useState<ProductGroup []>([])
    const [samplingStandard, setSamplingStandard] = useState<SamplingStandards []>([])
    const [reportData, setReportData] = useState<ReportData []>([])
    const navigate = useNavigate()
    const {setAlertDetails} = useContext(AlertContext);

    useEffect(() => {
        const getCodes = async() => {
            try{
                let response = await getAllCodes();
                console.log(response.data)
                if(response.status === 200){
                    setCodes(response.data)
                }
            }catch(err){
                console.log(err)
            }
        }

        const getClients = async() => {
            try{
                let response = await getAllClients();
                console.log(response.data)
                if(response.status === 200){
                    setClients(response.data)
                }
            }catch(err){
                console.log(err)
            }
        }

        const getInspections = async() => {
            try{
                let response = await getAllInspection();
                console.log(response.data)
                if(response.status === 200){
                    setInspections(response.data)
                }
            }catch(err){
                console.log(err)
            }
        }

        const getProductGroup = async() => {
            try{
                let response = await getAllGroup();
                console.log(response.data)
                if(response.status === 200){
                    setProductGroup(response.data)
                }
            }catch(err){
                console.log(err)
            }
        }

        const getSamplingStandard = async() => {
            try{
                let response = await getAllSamplingStandard();
                console.log(response.data)
                if(response.status === 200){
                    setSamplingStandard(response.data)
                }
            }catch(err){
                console.log(err)
            }
        }

        const getReportData = async() => {
            try{
                let response = await getAllReportData();
                console.log(response.data)
                if(response.status === 200){
                    setReportData(response.data)
                }
            }catch(err){
                console.log(err)
            }
        }

        getCodes()
        getClients()
        getInspections()
        getProductGroup()
        getSamplingStandard()
        getReportData()

    },[])

    const submit = async(values:any) => {
        console.log(errors)
        console.log(values)
        values.code = JSON.parse(values.code)
        values.client = JSON.parse(values.client)
        // values.inspection = JSON.parse(values.inspection)
        values.group = JSON.parse(values.group)
        values.samplingStandard = JSON.parse(values.samplingStandard)
        // values.reportData = JSON.parse(values.reportData)
        values.analysis = values.analysis === "true"? true:false;
        console.log(values)
        
        try{
            let response = await addSample(values)
            console.log(response)
            if(response.status === 201 || response.status === 200){
                setAlertDetails({isAlert:true, message:"Udało ci dodać próbkę", type:"success"})
                navigate("/")
            }
        }catch(err){
            console.log(err)
            setAlertDetails({isAlert:true, message:"Wystąpił bład spróbuj ponownie później", type:"error"})
        }
    }

    return(<div className='flex flex-col justify-center items-center w-full'>
        <h2 className="text-center font-bold my-3 text-2xl">Formularz dodawnia próbki</h2>
        <FormProvider {...method}>
            <form className="w-4/5 bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
                <div className='flex-col'>
                    <div className='flex justify-between p-5 bg-white rounded text-left w-100%'>
                        <div className='w-1/4'>
                            <h2 className='text-2xl font-bold'>Dane próbki</h2>
                            <FormLabel>Symbol próbki</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={codes.map(code=>({value: JSON.stringify(code), label:code.name}))}
                                {...register("code",{
                                    required:{
                                        value:true,
                                        message:"Pole wymagane"
                                    }})}
                            />
                            {errors.code && errors.code.message &&
                                <p className="text-red-600">{`${errors.code.message}`}</p>}

                            <FormLabel>Data przyjęcia próbki</FormLabel>
                            <Input type="date" {...register("admissionDate",{
                                required:{
                                    value:true,
                                    message:"Pole wymagane"
                                }})}
                            />
                            {errors.admissionDate && errors.admissionDate.message &&
                                <p className="text-red-600">{`${errors.admissionDate.message}`}</p>}

                            <FormLabel>Data przydatności</FormLabel>
                            <Input type="date" {...register("expirationDate",{
                                required:{
                                    value:true,
                                    message:"Pole wymagane"
                                }})}
                            />
                            {errors.expirationDate && errors.expirationDate.message &&
                                <p className="text-red-600">{`${errors.expirationDate.message}`}</p>}

                            <FormLabel>Planowana data zakończenia badań</FormLabel>
                            <Input type="date" {...register("examinationEndDate",{
                                required:{
                                    value:true,
                                    message:"Pole wymagane"
                                }})}
                            />
                            {errors.examinationEndDate && errors.examinationEndDate.message &&
                                <p className="text-red-600">{`${errors.examinationEndDate.message}`}</p>}

                        </div>
                        <div className='w-1/4'>
                            <FormLabel>Analiza odwoławcza</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={[{value: "true", label: "Tak"}, {value: "false", label: "Nie"}]}
                                {...register("analysis",{
                                    required:{
                                        value:true,
                                        message:"Pole wymagane"
                                    }})}
                            />
                            {errors.analysis && errors.analysis.message &&
                                <p className="text-red-600">{`${errors.analysis.message}`}</p>}

                            <FormLabel>Stan próbki</FormLabel>
                            <Input {...register("state",{
                                required:{
                                    value:true,
                                    message:"Pole wymagane"
                                }})}
                            />
                            {errors.state && errors.state.message &&
                                <p className="text-red-600">{`${errors.state.message}`}</p>}

                            <FormLabel>Grupa</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={productGroup.map(group=>({value: JSON.stringify(group), label:group.name}))}
                                {...register("group",{
                                    required:{
                                        value:true,
                                        message:"Pole wymagane"
                                    }})}
                            />
                            {errors.group && errors.group.message &&
                                <p className="text-red-600">{`${errors.group.message}`}</p>}

                            <FormLabel>Expiration Comment</FormLabel>
                            <Input {...register("expirationComment",{
                                required:{
                                    value:true,
                                    message:"Pole wymagane"
                                }})}
                            />
                            {errors.expirationComment && errors.expirationComment.message &&
                                <p className="text-red-600">{`${errors.expirationComment.message}`}</p>}

                            <FormLabel>Wybierz normę pobrania próbki</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={samplingStandard.map(standard=>({value: JSON.stringify(standard), label:standard.name}))}
                                {...register("samplingStandard",{
                                    required:{
                                        value:true,
                                        message:"Pole wymagane"
                                    }})}
                            />
                            {errors.samplingStandard && errors.samplingStandard.message &&
                                <p className="text-red-600">{`${errors.samplingStandard.message}`}</p>}
                        </div>
                        <div className='w-1/4'>
                            <h2 className='font-bold text-2xl'>Dane Klienta</h2>
                            <FormLabel>Nazwa Klienta</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={clients.map(client=>({value: JSON.stringify(client), label:client.name}))}
                                {...register("client",{
                                    required:{
                                        value:true,
                                        message:"Pole wymagane"
                                    }})}
                            />
                            {errors.client && errors.client.message &&
                                <p className="text-red-600">{`${errors.client.message}`}</p>}
                            {/* <FormLabel>Asortyment</FormLabel>
                    <Input {...register("assortment",{
                            required:{
                            value:true,
                            message:"Pole wymagane"
                        }})}
                    />

                    <FormLabel>Wielkość</FormLabel>
                    <Input {...register("size",{
                            required:{
                            value:true,
                            message:"Pole wymagane"
                        }})}
                    />

                    <FormLabel>Inspekcja</FormLabel>
                    <Select
                        className="my-custom-class"
                        options={inspections.map(inspection=>({value: JSON.stringify(inspection), label:inspection.name}))}
                        {...register("inspection",{
                            required:{
                            value:true,
                            message:"Pole wymagane"
                        }})}
                    /> */}
                        </div>
                    </div>
                    <div className='flex justify-center p-3 gap-5'>
                        <CancelButton type='button' className='mt-3' onClick={() => navigate('/')}>Anuluj</CancelButton>
                        <Button type="submit" className='mt-3'>Dodaj</Button>

                    </div>
                </div>

            </form>
        </FormProvider>
        
    </div>)
}

export default SampleForm;
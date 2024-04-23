import { FC, useEffect, useState, useRef } from 'react'
import { Input } from './ui/Input';
import { Select } from './ui/Select';
import { useForm } from 'react-hook-form';
import { FormLabel } from './ui/Labels';
import { Button } from './ui/Button';
import { getAllCodes } from '../helpers/codeApi';
import { getAllClients } from '../helpers/clientApi';
import { getAllInspection } from '../helpers/inspectionApi';
import { getAllGroup } from '../helpers/groupApi';
import { getAllSamplingStandard } from '../helpers/samplingStandardApi';
import { getAllReportData } from '../helpers/reportDataApi';
import { ListFormat } from 'typescript';
import { Code, Client, Inspection, ProductGroup, SamplingStandards, ReportData, Sample } from '../utils/types';
import { addSample } from '../helpers/samplingApi';
const SampleForm:FC<{}>=()=>{

    const { handleSubmit, register, formState: { errors } } = useForm();
    const [message, setMessage] = useState<String>("")
    const [codes, setCodes] = useState<Code []>([])
    const [clients, setClients] = useState<Client []>([])
    const [inspections, setInspections] = useState<Inspection []>([])
    const [productGroup, setProductGroup] = useState<ProductGroup []>([])
    const [samplingStandard, setSamplingStandard] = useState<SamplingStandards []>([])
    const [reportData, setReportData] = useState<ReportData []>([])

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
        values.code = JSON.parse(values.code)
        values.client = JSON.parse(values.client)
        values.inspection = JSON.parse(values.inspection)
        values.group = JSON.parse(values.group)
        values.samplingStandard = JSON.parse(values.samplingStandard)
        values.reportData = JSON.parse(values.reportData)
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
            <h2 className="text-center font-bold mb-3 text-2xl">Formularz dodawnia próbki</h2>
            <FormLabel>Kod</FormLabel>
            <Select
                className="my-custom-class"
                options={codes.map(code=>({value: JSON.stringify(code), label:code.name}))}
                {...register("code",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />
            {/* {errors.code && errors.code.message&&<p className="text-red-600">{errors.code.message}</p>} */}
            
            <FormLabel>Klient</FormLabel>
            <Select
                className="my-custom-class"
                options={clients.map(client=>({value: JSON.stringify(client), label:client.name}))}
                {...register("client",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />
            
            <FormLabel>Asortyment</FormLabel>
            <Input {...register("assortment",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Admission Date</FormLabel>
            <Input type="date" {...register("admissionDate",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Expiration Date</FormLabel>
            <Input type="date" {...register("expirationDate",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Expiration Comment</FormLabel>
            <Input {...register("expirationComment",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Examination EndDate</FormLabel>
            <Input type="date" {...register("examinationEndDate",{
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

            <FormLabel>Status</FormLabel>
            <Input {...register("state",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />
            
            <FormLabel>Analiza</FormLabel>
            <Select
                className="my-custom-class"
                options={[{value: true, label: "Tak"}, {value: false, label: "Nie"}]}
                {...register("analysis",{
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
            />

            <FormLabel>Grupa</FormLabel>
            <Select
                className="my-custom-class"
                options={productGroup.map(group=>({value: JSON.stringify(group), label:group.name}))}
                {...register("group",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Norma próbki</FormLabel>
            <Select
                className="my-custom-class"
                options={samplingStandard.map(standard=>({value: JSON.stringify(standard), label:standard.name}))}
                {...register("samplingStandard",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <FormLabel>Dane do raportu</FormLabel>
            <Select
                className="my-custom-class"
                options={reportData.map(data=>({value: JSON.stringify(data), label:data.deliveryMethod}))}
                {...register("reportData",{
                    required:{
                    value:true,
                    message:"Pole wymagane"
                }})}
            />

            <Button type="submit" className='mt-3'>Utwórz próbke</Button>
        </form>
    </div>)
}

export default SampleForm;
import {FC, useContext, useEffect, useState} from 'react'
import {Input} from './ui/Input';
import {FormSelect} from './ui/Select';
import {FormProvider, useForm} from 'react-hook-form';
import {FormLabel} from './ui/Labels';
import {CancelButton, StandardButton} from './ui/StandardButton';
import {getAllCodes} from '../helpers/codeApi';
import {getAllClients} from '../helpers/clientApi';
import {getAllInspection} from '../helpers/inspectionApi';
import {getAllGroup} from '../helpers/groupApi';
import {Assortment, Client, Code, Inspection, ProductGroup, SamplingStandards} from '../utils/types';
import {addSample} from '../helpers/samplingApi';
import {useNavigate, useParams} from 'react-router-dom';
import {AlertContext} from '../contexts/AlertsContext';
// import { Checkbox } from '@mui/material';
import {checkResponse} from '../utils/checkResponse';
import {getSampleById} from '../helpers/sampleApi';
import {LoadingSpinner} from "./ui/LoadingSpinner";

const SampleForm: FC<{}> = () => {

    const method = useForm();
    const {handleSubmit, register, watch, setValue, formState: {errors}, reset} = method;
    const [codes, setCodes] = useState<Code []>([]);
    const [clients, setClients] = useState<Client []>([]);
    const [inspections, setInspections] = useState<Inspection []>([]);
    const [groups, setGroups] = useState<ProductGroup []>([]);
    const navigate = useNavigate();
    const {setAlertDetails} = useContext(AlertContext);
    const chosenGroup: string = watch("group");
    const analysis: boolean = watch("analysis");
    const assortment: string = watch("assortment");
    const samplingStandard: string = watch("samplingStandard")
    const {sampleId} = useParams()

    useEffect(() => {
        const getSample = async () => {
            try {
                let response = await getSampleById(sampleId)
                let sample = response?.data
                sample.code = JSON.stringify(sample.code)
                sample.client = JSON.stringify(sample.client)
                sample.inspection = JSON.stringify(sample.inspection)
                sample.group = JSON.stringify(groups.filter(el => sample.assortment.group.name === el.name)[0])
                sample.samplingStandard = JSON.stringify(sample.samplingStandard)
                sample.assortment = JSON.stringify(sample.assortment);
                console.log(sample)
                reset(sample)
            } catch (err) {
                console.log(err);
                checkResponse(err);
            }
        }

        if (sampleId && codes.length && clients.length && inspections.length && groups.length) {
            getSample();
        }
    }, [codes, clients, inspections, groups])

    useEffect(() => {
        const getCodes = async () => {
            try {
                let response = await getAllCodes();
                console.log(response.data)
                if (response.status === 200) {
                    setCodes(response.data)
                }
            } catch (err) {
                console.log(err)
                checkResponse(err);
            }
        }

        const getClients = async () => {
            try {
                let response = await getAllClients();
                console.log(response.data)
                if (response.status === 200) {
                    setClients(response.data)
                }
            } catch (err) {
                console.log(err)
                checkResponse(err);
            }
        }

        const getInspections = async () => {
            try {
                let response = await getAllInspection();
                console.log(response.data)
                if (response.status === 200) {
                    setInspections(response.data)
                }
            } catch (err) {
                console.log(err)
                checkResponse(err);
            }
        }

        const getProductGroup = async () => {
            try {
                let response = await getAllGroup();
                console.log(response.data)
                if (response.status === 200) {
                    setGroups(response.data)
                }
            } catch (err) {
                console.log(err)
                checkResponse(err);
            }
        }

        getCodes()
        getClients()
        getInspections()
        getProductGroup()
    }, [])

    useEffect(() => {
        setValue("assortment", null)
        setValue("samplingStandard", null)
    }, [chosenGroup])

    const submit = async (values: any) => {
        values.code = JSON.parse(values.code)
        values.client = JSON.parse(values.client)
        values.inspection = JSON.parse(values.inspection)
        values.group = JSON.parse(values.group)
        values.samplingStandard = JSON.parse(values.samplingStandard)
        values.assortment = JSON.parse(values.assortment);
        // values.reportData = JSON.parse(values.reportData)
        // values.analysis = values.analysis === "true" ? true : false;
        console.log(values.analysis)
        try {
            let response = await addSample(values)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Udało ci dodać próbkę", type: "success"})
                navigate("/")
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił błąd, spróbuj ponownie później", type: "error"})
            checkResponse(err);
        }
    }
    const isReady = () => {
        console.log(assortment, samplingStandard)
        return codes.length && clients.length && inspections.length && groups.length;
    }
    return (isReady() ? <div className='flex flex-col justify-center items-center w-full'>
        <h2 className="text-center font-bold my-3 text-2xl">Dodawanie próbki</h2>
        <FormProvider {...method}>
            <form className="w-4/5 bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
                <div className='flex-col'>
                    <div className='flex justify-between p-5 bg-white rounded text-left w-100%'>
                        <div className='w-1/4'>
                            <h2 className='text-2xl font-bold'>Dane próbki</h2>
                            <FormLabel>Kod próbki</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={codes.map(code => ({value: JSON.stringify(code), label: code.name}))}
                                {...register("code", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                            />
                            {errors.code && errors.code.message &&
                                <p className="text-red-600">{`${errors.code.message}`}</p>}

                            <FormLabel>Kontrola</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={inspections.map(inspection => ({
                                    value: JSON.stringify(inspection),
                                    label: inspection.name
                                }))}
                                {...register("inspection", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                            />

                            {errors.inspection && errors.inspection.message &&
                                <p className="text-red-600">{`${errors.inspection.message}`}</p>}
                            <FormLabel>Data przyjęcia próbki</FormLabel>
                            <Input type="date" {...register("admissionDate", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane"
                                }
                            })}
                            />
                            {errors.admissionDate && errors.admissionDate.message &&
                                <p className="text-red-600">{`${errors.admissionDate.message}`}</p>}

                            <FormLabel>Data przydatności</FormLabel>
                            <Input type="date" {...register("expirationDate", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane"
                                }
                            })}
                            />
                            {errors.expirationDate && errors.expirationDate.message &&
                                <p className="text-red-600">{`${errors.expirationDate.message}`}</p>}

                            <FormLabel>Dodatkowy komentarz</FormLabel>
                            <Input {...register("expirationComment", {})}
                            />
                            {errors.expirationComment && errors.expirationComment.message &&
                                <p className="text-red-600">{`${errors.expirationComment.message}`}</p>}

                            <FormLabel>Planowana data zakończenia badań</FormLabel>
                            <Input type="date" {...register("examinationExpectedEndDate", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane"
                                }
                            })}
                            />
                            {errors.examinationExpectedEndDate && errors.examinationExpectedEndDate.message &&
                                <p className="text-red-600">{`${errors.examinationExpectedEndDate.message}`}</p>}
                        </div>
                        <div className='w-1/4'>
                            <h2 className='text-2xl font-bold opacity-0 cursor-normal'>D</h2>
                            {/* <FormSelect
                                className="my-custom-class"
                                options={[{value: "true", label: "Tak"}, {value: "false", label: "Nie"}]}
                                {...register("analysis", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                            /> */}

                            <FormLabel>Grupa</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={groups.map(group => ({value: JSON.stringify(group), label: group.name}))}
                                {...register("group", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                            />
                            {errors.group && errors.group.message &&
                                <p className="text-red-600">{`${errors.group.message}`}</p>}

                            <FormLabel>Asortyment</FormLabel>
                            <FormSelect
                                isDisabled={!chosenGroup}
                                chosenGroup={chosenGroup}
                                className="my-custom-class"
                                options={JSON.parse(chosenGroup ? chosenGroup : "{}")?.assortments?.map((assortment: Assortment) => ({
                                    value: JSON.stringify(assortment),
                                    label: assortment.name
                                }))}
                                {...register("assortment", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                            />
                            {errors.assortment && errors.assortment.message &&
                                <p className="text-red-600">{`${errors.assortment.message}`}</p>}

                            <FormLabel>Norma pobrania próbki</FormLabel>
                            <FormSelect
                                isDisabled={!chosenGroup}
                                chosenGroup={chosenGroup}
                                className="my-custom-class"
                                options={JSON.parse(chosenGroup ? chosenGroup : "{}")?.samplingStandards?.map((standard: SamplingStandards) => ({
                                    value: JSON.stringify(standard),
                                    label: standard.name
                                }))}
                                {...register("samplingStandard", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                            />
                            {errors.samplingStandard && errors.samplingStandard.message &&
                                <p className="text-red-600">{`${errors.samplingStandard.message}`}</p>}

                            <FormLabel>Stan próbki</FormLabel>
                            <Input {...register("state", {})}/>
                            {errors.state && errors.state.message &&
                                <p className="text-red-600">{`${errors.state.message}`}</p>}

                            <FormLabel>Wielkość próbki</FormLabel>
                            <Input {...register("size", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane"
                                }
                            })}
                            />
                            {errors.size && errors.size.message &&
                                <p className="text-red-600">{`${errors.size.message}`}</p>}

                            <div className='flex items-center justify-between'>
                                <label className='form-label text-mb' style={{lineHeight: '1.5rem'}}>Analiza
                                    odwoławcza</label>
                                <input type="checkbox" className='h-4 w-4 rounded'
                                       {...register("analysis", {})}
                                       checked={analysis}
                                />
                            </div>
                        </div>
                        <div className='w-1/4'>
                            <h2 className='font-bold text-2xl'>Dane Klienta</h2>
                            <FormLabel>Nazwa Klienta</FormLabel>
                            <FormSelect
                                className="my-custom-class"
                                options={clients.map(client => ({value: JSON.stringify(client), label: client.name}))}
                                {...register("client", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
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
                    <div className='flex justify-center p-3 gap-2'>
                        <CancelButton type='button' className='mt-3' onClick={() => navigate('/')}>Anuluj</CancelButton>
                        <StandardButton type="submit" className='mt-3'>{sampleId ? "Edytuj" : "Dodaj"}</StandardButton>
                    </div>
                </div>

            </form>
        </FormProvider>

    </div> : <LoadingSpinner/>)
}

export default SampleForm;
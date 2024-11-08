import React, {FC, useEffect, useState} from "react";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {addExamination, getExaminationById, updateExamination} from "../helpers/examinationApi";
import {FormProvider, useForm} from "react-hook-form";
import {FormLabel} from "./ui/Labels";
import {BigTextInput, Input} from "./ui/Input";
import {ExaminationFromSelect} from "./ui/Select";
import {CancelButton, StandardButton} from "./ui/StandardButton";
import {getIndicationById} from "../helpers/indicationApi";
import {Examination, Indication, Sample} from "../utils/types";
import {getSampleById} from "../helpers/sampleApi";
import { checkResponse } from "../utils/checkResponse";

type ExaminationFormFields = {
    signage: string,
    nutritionalValue: string,
    specification: string,
    regulation: string,
    samplesNumber: number,
    result: string,
    startDate: Date,
    endDate: Date,
    methodStatus: string,
    uncertainty: number,
    lod: number,
    loq: number
}

enum MethodStatuses {
    A = "(A)",
    AE = "(AE)",
    N = "(N)"
}

const ExaminationForm: FC<{}> = () => {

    const {sampleId, examinationId} = useParams();
    const navigate = useNavigate()
    const location = useLocation();
    const {indicationId} = location.state || {};

    const [examination, setExamination] = useState<Examination | null>(null);
    const [indication, setIndication] = useState<Indication | null>(null);
    const [sample, setSample] = useState<Sample | null>(null);

    const methods = useForm({
        defaultValues: {
            signage: examination?.signage || '',
            nutritionalValue: examination?.nutritionalValue || '',
            specification: examination?.specification || '',
            regulation: examination?.regulation || '',
            samplesNumber: examination?.samplesNumber || '',
            result: examination?.result || '',
            startDate: examination?.startDate || '',
            endDate: examination?.endDate || '',
            methodStatus: examination?.methodStatus || '',
            uncertainty: examination?.uncertainty || '',
            lod: examination?.lod || '',
            loq: examination?.loq || '',
        },
    });

    const {handleSubmit, register, formState: {errors}, setValue} = methods

    useEffect(() => {
        if (examinationId) {
            const getExamination = async () => {
                try {
                    let response = await getExaminationById(examinationId);
                    if (response?.status === 200) {
                        setExamination(response.data);
                    }
                } catch (err) {
                    console.log(err);
                    checkResponse(err);
                }
            };

            getExamination();
        }

        if (indicationId) {
            const getIndication = async () => {
                try {
                    let response = await getIndicationById(indicationId);
                    if (response?.status === 200) {
                        setIndication(response.data);
                    }
                } catch (err) {
                    console.log(err);
                    checkResponse(err);
                }
            }

            getIndication();
        }

        if (sampleId) {
            const getSample = async () => {
                try {
                    let response = await getSampleById(sampleId);
                    if (response?.status === 200) {
                        setSample(response.data);
                    }
                } catch (err) {
                    console.log(err);
                    checkResponse(err);
                }
            }

            getSample();
        }
    }, [examinationId, indicationId, sampleId, getExaminationById, getIndicationById, getSampleById]);

    const fieldsToSet: Array<keyof ExaminationFormFields> = [
        'signage',
        'nutritionalValue',
        'specification',
        'regulation',
        'samplesNumber',
        'result',
        'startDate',
        'endDate',
        'methodStatus',
        'uncertainty',
        'lod',
        'loq',
    ]

    useEffect(() => {
        fieldsToSet.forEach(field => {
            if (examination && examination[field]) {
                setValue(field, examination[field]);
            }
        });
    }, [examination, setValue]);

    const submit = async (values: any) => {

        if (examination) {
            values = {
                ...values,
                id: examinationId,
                indication,
                sample,
            }

            try {
                console.log("cipa");
                console.log(values);
                let response = await updateExamination(values);
                console.log(response);
            } catch (err) {
                console.log(err);
                checkResponse(err);
            }
        } else {
            values = {
                ...values,
                indication,
                sample,
            }

            try {
                let response = await addExamination(values);
                console.log(response);
            } catch (err) {
                console.log(err);
                checkResponse(err);
            }
        }

        navigate(`/sample/manageExaminations/${sampleId}`);
    };

    return (<div className='flex flex-col justify-center items-center w-full'>
        <h1 className="text-center font-bold my-10 text-2xl">Dodawanie wyników badań</h1>
        {indication && <h3 className="text-center font-bold text-2xl">{indication.name}</h3>}
        <FormProvider {...methods}>
            <form className="w-4/5 p-5 bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
                <div className='flex-col'>

                    {!indication?.isOrganoleptic && <div className='flex justify-between p-5 bg-white rounded text-left w-100%'>
                        <div className='w-1/2'>
                            <FormLabel>Oznakowanie</FormLabel>
                            <Input {...register("signage")}
                            />

                            <FormLabel>Wartość odżywcza</FormLabel>
                            <Input {...register("nutritionalValue")}
                            />

                            <FormLabel>Specyfikacja</FormLabel>
                            <Input {...register("specification")}
                            />

                            <FormLabel>Rozporządzenie</FormLabel>
                            <Input {...register("regulation")}
                            />

                            <FormLabel>Liczba próbek do badania</FormLabel>
                            <Input type="number" {...register("samplesNumber", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane"
                                }
                            })}
                            />
                            {errors.samplesNumber && <p className="text-red-600">{`${errors.samplesNumber.message}`}</p>}
                        </div>

                        <div className='w-1/4 flex flex-col'>

                            <FormLabel>Wynik badania</FormLabel>
                            <Input {...register("result")}
                            />

                            <FormLabel>Data rozp. badania</FormLabel>
                            <Input type="date" {...register("startDate")}
                            />

                            <FormLabel>Data zakoń. badania</FormLabel>
                            <Input type="date" {...register("endDate")}
                            />

                            <FormLabel>Status metody</FormLabel>
                            <ExaminationFromSelect
                                className="my-custom-class"
                                options={Object.values(MethodStatuses).map(methodStatus => ({
                                    value: methodStatus,
                                    label: methodStatus,
                                }))}
                                {...register("methodStatus")}
                            />

                            <FormLabel>Niepewność</FormLabel>
                            <Input {...register("uncertainty")}
                            />

                            <FormLabel>LOD</FormLabel>
                            <Input {...register("lod")}
                            />

                            <FormLabel>LOQ</FormLabel>
                            <Input {...register("loq")}
                            />
                        </div>
                    </div>}

                    {indication?.isOrganoleptic && <div className='flex justify-between p-5 bg-white rounded text-left w-100%'>

                        <div className='w-1/2'>
                            <FormLabel>Wymagania</FormLabel>
                            <BigTextInput
                                {...register("signage", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                                rows={4}
                                style={{ resize: "none" }}
                            />
                            {errors.signage && <p className="text-red-600">{`${errors.signage.message}`}</p>}

                            <FormLabel>Wynik badania</FormLabel>
                            <BigTextInput
                                {...register("result")}
                                rows={4}
                                style={{ resize: "none" }}
                            />
                        </div>

                        <div className='w-1/4 flex flex-col'>
                            <FormLabel>Liczba próbek do badania</FormLabel>
                            <Input type="number" {...register("samplesNumber", {
                                required: {
                                    value: true,
                                    message: "Pole wymagane"
                                }
                            })}
                            />
                            {errors.samplesNumber && <p className="text-red-600">{`${errors.samplesNumber.message}`}</p>}

                            <FormLabel>Data rozp. badania</FormLabel>
                            <Input type="date" {...register("startDate")}
                            />

                            <FormLabel>Data zakoń. badania</FormLabel>
                            <Input type="date" {...register("endDate")}
                            />

                            <FormLabel>Status metody</FormLabel>
                            <ExaminationFromSelect
                                className="my-custom-class"
                                options={Object.values(MethodStatuses).map(methodStatus => ({
                                    value: methodStatus,
                                    label: methodStatus,
                                }))}
                                {...register("methodStatus")}
                            />

                        </div>

                    </div>}

                    <div className='flex justify-center gap-5'>
                        <CancelButton type='button' className='mt-3'
                                      onClick={() => navigate(`/sample/manageExaminations/${sampleId}`)}>Anuluj</CancelButton>
                        <StandardButton type="submit" className='mt-3 justify-self-end'>Zapisz</StandardButton>
                    </div>
                </div>

            </form>
        </FormProvider>

    </div>)
}

export default ExaminationForm;

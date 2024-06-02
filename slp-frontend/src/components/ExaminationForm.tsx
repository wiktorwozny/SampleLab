import {FC, useEffect, useState} from "react";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {addExamination, getExaminationById, updateExamination} from "../helpers/examinationApi";
import {FormProvider, useForm} from "react-hook-form";
import {FormLabel} from "./ui/Labels";
import {Input} from "./ui/Input";
import {FormSelect} from "./ui/Select";
import {Button} from "./ui/Button";
import {getIndicationById} from "../helpers/indicationApi";
import {Examination, Indication, Sample} from "../utils/types";
import {Simulate} from "react-dom/test-utils";
import reset = Simulate.reset;
import {getSampleById} from "../helpers/sampleApi";

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
                let response = await updateExamination(examination.id, values);
                console.log(response);
            } catch (err) {
                console.log(err);
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
            }
        }

        navigate(`/sample/manageExaminations/${sampleId}`);
    };

    return (<div className='flex flex-col justify-center items-center'>
        <h1 className="text-center font-bold my-10 text-2xl">Dodawanie wyników badań</h1>
        {indication && <h3 className="text-center font-bold mb-5 text-2xl">{indication.name}</h3>}
        <FormProvider {...methods}>
            <form className="w-4/5 flex justify-between p-5 bg-white rounded text-left" onSubmit={handleSubmit(submit)}>
                <div className='w-1/2'>
                    <FormLabel>Oznakowanie</FormLabel>
                    <Input {...register("signage", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />

                    <FormLabel>Wartość odżywcza</FormLabel>
                    <Input {...register("nutritionalValue", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />

                    <FormLabel>Specyfikacja</FormLabel>
                    <Input {...register("specification", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />

                    <FormLabel>Rozporządzenie</FormLabel>
                    <Input {...register("regulation", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />

                    <FormLabel>Liczba próbek do badania</FormLabel>
                    <Input type="number" {...register("samplesNumber", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                    />
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
                    <FormSelect
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

                    <Button type="submit" className='mt-3 w-1/2 justify-self-end'>Zapisz</Button>
                </div>
            </form>
        </FormProvider>
        
    </div>)
}

export default ExaminationForm;

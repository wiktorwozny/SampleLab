import {FC, RefObject, useContext, useEffect, useRef, useState} from "react";
import {Address, ReportData} from "../utils/types";
import {FormProvider, useForm} from 'react-hook-form';
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {AlertContext} from "../contexts/AlertsContext";
import {getAllAddresses} from "../helpers/addressApi";
import {FormLabel} from "./ui/Labels";
import {Input} from "./ui/Input";
import {AddressController} from "./ui/AddressController";
import {FormSelect} from "./ui/Select";
import {CancelButton, StandardButton} from "./ui/StandardButton";
import {addReportData, getReportDataBySampleId} from "../helpers/reportDataApi";
import ReportDataForm from "./ReportDataForm";

interface UncommonInfo {
    batchSizeProd: string;
    batchSizeStorehouse: string;
    productionDate: Date;
    batchNumber: number;
    samplePacking: string;
}

interface CommonInfo {
    manufacturerName: string;
    manufacturerAddress: Address;
    manufacturerCountry: string;
    supplierName: string;
    supplierAddress: Address;
    sellerName: string;
    sellerAddress: Address;
    recipientName: string;
    recipientAddress: Address;
    sampleCollectionSite: string;
    jobNumber: number;
    mechanism: string;
    deliveryMethod: string;
}

interface FormData {
    uncommonInfo: UncommonInfo[];
    commonInfo: CommonInfo;
}


const ProtocolReportDataForm: FC<{}> = ({}) => {

    const [existingReportDataIds, setExistingReportDataIds] = useState<(number | null)[]>([]);
    const method = useForm();
    const {handleSubmit, register, formState: {errors}, setValue} = method;
    const [addresses, setAddresses] = useState<Address []>([]);
    const [isSeller, setIsSeller] = useState<Boolean>(true);
    const navigate = useNavigate();
    const {setAlertDetails} = useContext(AlertContext);
    const [countries, setCountries] = useState<string[]>([]);
    const sellerInputRef:RefObject<HTMLInputElement> = useRef<HTMLInputElement>(null);

    const {data} = useParams();
    const [protocolSamplesIds, setProtocolSamplesIds] = useState<number[]>([]);

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
        };

        const getProtocolSamplesIds = () => {
            if (data) {
                const parsed = JSON.parse(decodeURIComponent(data));
                if (Array.isArray(parsed)) {
                    const sorted = [...parsed].sort((a, b) => a - b);
                    setProtocolSamplesIds(sorted);
                    return sorted;
                } else {
                    console.error('Parsed data is not an array');
                    return [];
                }
            }
            return [];
        };

        const fetchExistingReportData = async (sampleIds: number[]) => {
            try {
                const reportDataPromises = sampleIds.map(sampleId =>
                    getReportDataBySampleId(sampleId.toString())
                        .then(response => response.data.id)
                        .catch(() => null)
                );
                const reportDataIds = await Promise.all(reportDataPromises);
                setExistingReportDataIds(reportDataIds);
            } catch (error) {
                console.error('Error fetching existing report data:', error);
                setAlertDetails({ isAlert: true, message: 'Błąd podczas pobierania istniejących danych', type: 'error' });
            }
        };

        getAddresses();
        const sampeIds = getProtocolSamplesIds();
        fetchExistingReportData(sampeIds);

        if(sellerInputRef.current){
            sellerInputRef.current.checked = true;
        }
    }, [data, sellerInputRef])

    interface Country {
        name: {
            common: string;
        };
        translations: {
            pol?: {
                common: string;
            };
        };
    }

    useEffect(() => {
        fetch('https://restcountries.com/v3.1/all')
            .then(response => response.json())
            .then((data: Country[]) => {
                const countryNames = data.map((country) =>
                    country.translations?.pol?.common || country.name.common
                );
                setCountries(countryNames);
            })
            .catch(error => console.error('Error fetching countries:', error));
    }, []);

    const submit = async (values: any) => {
        console.log(values);
        console.log(existingReportDataIds);
        try {
            const commonData = {
                manufacturerName: values.manufacturerName,
                manufacturerAddress: values.manufacturerAddress,
                manufacturerCountry: values.manufacturerCountry,
                supplierName: values.supplierName,
                supplierAddress: values.supplierAddress,
                sellerName: values.sellerName,
                sellerAddress: values.sellerAddress,
                recipientName: values.recipientName,
                recipientAddress: values.recipientAddress,
                sampleCollectionSite: values.sampleCollectionSite,
                jobNumber: values.jobNumber,
                mechanism: values.mechanism,
                deliveryMethod: values.deliveryMethod,
            };

            const reportDataPromises = protocolSamplesIds.map(async (sampleId, index) => {
                const reportData: any = {
                    ...commonData,
                    productionDate: new Date(values.productionDate[index]),
                    batchNumber: values.batchNumber[index],
                    batchSizeProd: values.batchSizeProd[index],
                    batchSizeStorehouse: values.batchSizeStorehouse[index],
                    samplePacking: values.samplePacking[index],
                    sampleId: sampleId
                };

                if (existingReportDataIds[index]) {
                    reportData.id = existingReportDataIds[index]
                }

                return addReportData(reportData);
            });

            const results = await Promise.all(reportDataPromises);

            console.log('All report data submitted successfully:', results);
            setAlertDetails({isAlert: true, message: "Udało ci się dodać dodatkowe informacje", type: "success"});
            navigate('/');
        } catch (error) {
            console.error('Error submitting report data:', error);
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"});
        }
    }

    return (
        <div className='flex flex-col justify-center items-center w-full'>
            <h2 className="text-center font-bold mt-10 text-2xl">Dodawanie dodatkowych informacji</h2>
            <FormProvider {...method}>
                <form className="w-100 p-5 bg-white rounded text-left" onSubmit={handleSubmit(submit)}>

                    <div className='flex-col'>
                        {protocolSamplesIds.map((field, index) => (
                            <div className='flex justify-between p-3 bg-white rounded text-left w-100% mb-0'>
                                <div className="flex items-center">
                                    <p className="my-auto">{field}: </p>
                                </div>
                                <div className='w-1/6'>
                                    {index === 0 && (<FormLabel>Wielk. partii prod.</FormLabel>)}
                                    <Input
                                        defaultValue='nie dotyczy'
                                        {...register(`batchSizeProd.${index}`, {
                                            required: {
                                                value: true,
                                                message: "Pole wymagane"
                                            }
                                        })}
                                    />
                                    {errors.batchSizeProd && errors.batchSizeProd.message &&
                                        <p className="text-red-600">{`${errors.batchSizeProd.message}`}</p>}
                                </div>
                                <div className='w-1/6'>
                                    {index === 0 && <FormLabel>Wielk. partii magazyn.</FormLabel>}
                                    <Input
                                        defaultValue='nie dotyczy'
                                        {...register(`batchSizeStorehouse.${index}`, {
                                            required: {
                                                value: true,
                                                message: "Pole wymagane"
                                            }
                                        })}
                                    />
                                    {errors.batchSizeStorehouse && errors.batchSizeStorehouse.message &&
                                        <p className="text-red-600">{`${errors.batchSizeStorehouse.message}`}</p>}
                                </div>
                                <div className='w-1/6'>
                                    {index === 0 && <FormLabel>Data produkcji</FormLabel>}
                                    <Input type="date" {...register(`productionDate.${index}`, {
                                        required: {
                                            value: true,
                                            message: "Pole wymagane"
                                        }
                                    })}
                                    />
                                    {errors.productionDate && errors.productionDate.message &&
                                        <p className="text-red-600">{`${errors.productionDate.message}`}</p>}
                                </div>
                                <div className='w-1/6'>
                                    {index === 0 && <FormLabel>Numer partii</FormLabel>}
                                    <Input {...register(`batchNumber.${index}`, {
                                        required: {
                                            value: true,
                                            message: "Pole wymagane"
                                        }
                                    })}
                                    />
                                    {errors.batchNumber && errors.batchNumber.message &&
                                        <p className="text-red-600">{`${errors.batchNumber.message}`}</p>}
                                </div>
                                <div className='w-1/6'>
                                    {index === 0 && <FormLabel>Opakowanie</FormLabel>}
                                    <Input {...register(`samplePacking.${index}`, {
                                        required: {
                                            value: true,
                                            message: "Pole wymagane"
                                        }
                                    })}
                                    />
                                    {errors.samplePacking && errors.samplePacking.message &&
                                        <p className="text-red-600">{`${errors.samplePacking.message}`}</p>}
                                </div>
                            </div>
                            ))}
                    </div>

                    <div className='flex-col'>
                        <div className='flex justify-between p-5 bg-white rounded text-left w-100%'>
                            <div className='w-5/12'>
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

                                <FormLabel>Kraj pochodzenia producenta</FormLabel>
                                <FormSelect
                                    className="my-custom-class"
                                    options={countries.map(country => ({value: country, label: country}))}
                                    {...register("manufacturerCountry", {
                                        required: {
                                            value: true,
                                            message: "Pole wymagane"
                                        }
                                    })}
                                />
                                {errors.manufacturerCountry && errors.manufacturerCountry.message &&
                                    <p className="text-red-600">{`${errors.manufacturerCountry.message}`}</p>}

                                <div className='flex'>
                                    <div className='flex mr-7'>
                                        <input className="form-check-input" type="radio" name="typeOfAddress"
                                               ref={sellerInputRef}
                                               onChange={val => {
                                                   setIsSeller(true)
                                               }}/>
                                        <div>&nbsp;sprzedawca</div>
                                    </div>
                                    <div className='flex'>
                                        <input className="form-check-input" type="radio" name="typeOfAddress"
                                               onChange={val => {
                                                   setIsSeller(false)
                                               }}/>
                                        <div>&nbsp;dostawca</div>
                                    </div>
                                </div>

                                {!isSeller && <>
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


                                {isSeller && <><FormLabel>Nazwa sprzedawcy</FormLabel>
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

                            <div className='w-5/12 flex flex-col'>
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
                                <FormLabel>Nazwa i symbol mechanizmu</FormLabel>
                                <Input {...register("mechanism", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                                />
                                {errors.mechanism && errors.mechanism.message &&
                                    <p className="text-red-600">{`${errors.mechanism.message}`}</p>}

                                <FormLabel>Sposób dostarczenia próbki</FormLabel>
                                <Input {...register("deliveryMethod", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                                />
                                {errors.deliveryMethod && errors.deliveryMethod.message &&
                                    <p className="text-red-600">{`${errors.deliveryMethod.message}`}</p>}
                                <h2 className='text-2xl font-bold p-2'>Dane próbkobiorcy</h2>
                                <FormLabel>Próbkę pobrał</FormLabel>
                                <Input
                                />
                                {errors.deliveryMethod && errors.deliveryMethod.message &&
                                    <p className="text-red-600">{`${errors.deliveryMethod.message}`}</p>}
                                <FormLabel>Miejsce pobrania próbki</FormLabel>
                                <Input {...register("sampleCollectionSite", {
                                    required: {
                                        value: true,
                                        message: "Pole wymagane"
                                    }
                                })}
                                />
                                {errors.sampleCollectionSite && errors.sampleCollectionSite.message &&
                                    <p className="text-red-600">{`${errors.sampleCollectionSite.message}`}</p>}
                            </div>
                        </div>

                        <div className='flex justify-center p-2 gap-5'>
                            <CancelButton type='button' className='mt-3'
                                          onClick={() => navigate(`/`)}>Anuluj</CancelButton>
                            <StandardButton type="submit" className='mt-3 justify-self-end'>Dodaj</StandardButton>
                        </div>

                    </div>

                </form>

            </FormProvider>
        </div>
    )
}

export default ProtocolReportDataForm;
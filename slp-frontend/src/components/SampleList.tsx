import {useEffect, useState} from "react"
import {FilterRequest, SummarySample} from "../utils/types"
import {getFilteredSamples} from "../helpers/samplingApi"
import {useNavigate} from "react-router-dom"
import {
    MdKeyboardArrowLeft,
    MdKeyboardArrowRight,
    MdKeyboardDoubleArrowLeft,
    MdKeyboardDoubleArrowRight
} from "react-icons/md";

import {FormProvider, useForm} from 'react-hook-form';
import {ProgressStateEnum, ProgressStateEnumDesc} from "../utils/enums";
import {ProgressFormSelect} from "./ui/ProgressFormSelect";


const SampleList: React.FC<any> = ({selectedFilters}) => {
    const progressEnumDesc = ProgressStateEnumDesc;
    const method = useForm();
    const {handleSubmit, register, formState: {errors}} = method
    const navigate = useNavigate()
    const [samples, setSamples] = useState<SummarySample[]>([])
    const [isLoading, setIsLoading] = useState<boolean>(true)
    const [request, setRequest] = useState<FilterRequest>({
        fieldName: 'id',
        ascending: true,
        pageNumber: 0,
        pageSize: 10,
        filters: selectedFilters
    })
    const [activeColumn, setActiveColumn] = useState<string>('id');
    const [numberOfPages, setNumberOfPages] = useState<number>(0);

    useEffect(() => {
        setRequest(prev => ({
            ...prev,
            filters: selectedFilters,
            pageNumber: 0
        }))
    }, [selectedFilters])

    useEffect(() => {
        const getSamples = async () => {
            try {
                let response = await getFilteredSamples(request);
                if (response.status === 200) {
                    setSamples(response.data.samples)
                    console.log(response.data.samples)
                    setNumberOfPages(response.data.totalPages)
                    setIsLoading(false)
                }
            } catch (err) {
                console.log(err);
            }
        }

        getSamples();
    }, [request])

    const updateSortParams = (newFieldName: string) => {
        const ascending = request.fieldName === newFieldName ? !request.ascending : true;
        setRequest(prevRequest => ({
            ...prevRequest,
            fieldName: newFieldName,
            ascending: ascending
        }));
        setActiveColumn(newFieldName);
    }

    const updatePageNumber = (pageNumber: number) => {
        setRequest(prevRequest => ({
            ...prevRequest,
            pageNumber: pageNumber
        }));
    }

    return (
        <div className="w-full">
            {!isLoading && numberOfPages > 0 && <div>
                <table className="table table-hover table-bordered cursor-pointer">
                    <thead>
                    <tr>
                        <th scope="col" className={activeColumn === 'id' ? '!bg-gray-400' : '!bg-gray-300'}
                            onClick={() => updateSortParams("id")}>ID
                        </th>
                        <th scope="col" className={activeColumn === 'code.id' ? '!bg-gray-400' : '!bg-gray-300'}
                            onClick={() => updateSortParams("code.id")}>Kod próbki
                        </th>
                        <th scope="col" className={activeColumn === 'group.name' ? '!bg-gray-400' : '!bg-gray-300'}
                            onClick={() => updateSortParams("group.name")}>Grupa
                        </th>
                        <th scope="col" className={activeColumn === 'assortment' ? '!bg-gray-400' : '!bg-gray-300'}
                            onClick={() => updateSortParams("assortment")}>Asortyment
                        </th>
                        <th scope="col" className={activeColumn === 'client.name' ? '!bg-gray-400' : '!bg-gray-300'}
                            onClick={() => updateSortParams("client.name")}>Nazwa klienta
                        </th>
                        <th scope="col" className={activeColumn === 'admissionDate' ? '!bg-gray-400' : '!bg-gray-300'}
                            onClick={() => updateSortParams("admissionDate")}>Data przyjęcia
                        </th>
                        <th scope="col" className={activeColumn === 'progress' ? '!bg-gray-400' : '!bg-gray-300'}
                            onClick={() => updateSortParams("progress")}>Progres
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {samples.map(sample => (
                        <tr
                            key={sample.id} onClick={() => {
                            if (sample.progressStatus !== ProgressStateEnum.TODO && sample.progressStatus !== null) navigate(`/sample/${sample.id}`)
                        }}
                        >
                            <td>{sample.id}</td>
                            <td>{sample.code}</td>
                            <td>{sample.group}</td>
                            <td>{sample.assortment}</td>
                            <td>{sample.clientName}</td>
                            <td>{sample.admissionDate.toString()}</td>
                            <td className="w-64 h-max"
                                onClick={(e) => {
                                    e.stopPropagation();
                                }}
                            >
                                <FormProvider {...method}>
                                    <ProgressFormSelect
                                        className="shadow-none h-max"
                                        options={progressEnumDesc.map(desc => ({value: desc.value, label: desc.label}))}
                                        sample={sample}
                                        defaultValue={
                                            progressEnumDesc.filter((obj) => obj.value === sample.progressStatus)
                                        }
                                        {...register("analysis", {})}
                                    />
                                    {errors.analysis && errors.analysis.message &&
                                        <p className="text-red-600">{`${errors.analysis.message}`}</p>}

                                </FormProvider>
                            </td>
                        </tr>))}
                    </tbody>
                </table>
                <div className="flex flex-row justify-center items-center">
                    <div className="mr-5">Pokaż: <input className="w-10 border" type="number"
                                                        defaultValue={request.pageSize} onChange={(e) => {
                        setRequest(prevRequest => ({
                            ...prevRequest,
                            pageSize: Number(e.target.value) > 0 ? Number(e.target.value) : request.pageSize,
                            pageNumber: 0
                        }));
                    }}/></div>
                    <MdKeyboardDoubleArrowLeft className="bg-gray-200" onClick={() => updatePageNumber(0)}/>
                    <MdKeyboardArrowLeft className="bg-gray-200 mx-1"
                                         onClick={() => updatePageNumber(Math.max(0, request.pageNumber - 1))}/>
                    <div>Strona {request.pageNumber + 1} z {numberOfPages}</div>
                    <MdKeyboardArrowRight className="bg-gray-200 mx-1"
                                          onClick={() => updatePageNumber(Math.min(numberOfPages - 1, request.pageNumber + 1))}/>
                    <MdKeyboardDoubleArrowRight className="bg-gray-200"
                                                onClick={() => updatePageNumber(numberOfPages - 1)}/>
                </div>
                <br/>
            </div>}
            {isLoading && <div className="text-2xl">Loading...</div>}
            {!isLoading && numberOfPages == 0 && <div className="text-2xl">Brak próbek spełniających filtry</div>}
        </div>
    )
}

export default SampleList
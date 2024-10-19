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

import {useForm} from 'react-hook-form';
import {ProgressStateEnum, ProgressStateMap} from "../utils/enums";
import {checkResponse} from "../utils/checkResponse";
import {DisableButton} from "./ui/StandardButton";


const SampleList: React.FC<any> = ({selectedFilters}) => {
    const progressEnumDesc = ProgressStateMap;
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
    const [selectedSamplesIds, setSelectedSamplesIds] = useState<number[]>([]);

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
                    setNumberOfPages(response.data.totalPages)
                    setIsLoading(false)
                }
            } catch (err) {
                checkResponse(err);
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
    };

    const updatePageNumber = (pageNumber: number) => {
        setRequest(prevRequest => ({
            ...prevRequest,
            pageNumber: pageNumber
        }));
    };

    const handleCheckboxChange = (sampleId: number) => {
        setSelectedSamplesIds((prevSelected) =>
            prevSelected.includes(sampleId) ? prevSelected.filter((id) => id !== sampleId) : [...prevSelected, sampleId]
        );
        console.log(selectedSamplesIds);
    };

    const isSampleSelected = (sampleId: number) => selectedSamplesIds.includes(sampleId);

    return (
        <div className="w-full">
            {!isLoading && numberOfPages > 0 && <div>
                <table className="table table-hover table-bordered cursor-pointer">
                    <thead>
                    <tr>
                        <th></th>
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
                        <th scope="col" className={activeColumn === 'progressStatus' ? '!bg-gray-400' : '!bg-gray-300'}
                            onClick={() => updateSortParams("progressStatus")}>Postęp
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {samples.map(sample => (
                        <tr
                            key={sample.id} onClick={() => navigate(`/sample/${sample.id}`)}
                        >
                            <td
                                style={{padding: 5, textAlign: 'center', width: 35, height: 35}}
                                onClick={(e) => e.stopPropagation()}>
                                <input
                                    type="checkbox"
                                    checked={isSampleSelected(sample.id)}
                                    onChange={() => handleCheckboxChange(sample.id)}
                                    onClick={(e) => e.stopPropagation()}
                                    style={{width: '80%', height: '80%'}}
                                />
                            </td>
                            <td>{sample.id}</td>
                            <td>{sample.code}</td>
                            <td>{sample.group}</td>
                            <td>{sample.assortment}</td>
                            <td>{sample.clientName}</td>
                            <td>{sample.admissionDate.toString()}</td>
                            <td className={(sample.progressStatus === ProgressStateEnum.DONE ? '!bg-green-100' : '!bg-red-100')}>{progressEnumDesc.get(sample.progressStatus)}</td>
                        </tr>))}
                    </tbody>
                </table>
                <div className="flex flex-row justify-between items-center w-full">

                    <DisableButton type="button" disabled={selectedSamplesIds.length === 0} onClick={(e) => {
                        e.stopPropagation();
                        const encoded = encodeURIComponent(JSON.stringify(selectedSamplesIds));
                        navigate(`/protocolReportData/${encoded}`);
                    }
                    }>
                        Wprowadź dodatkowe dane
                    </DisableButton>

                    <div className="flex flex-row justify-center items-center w-full">
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

                    <button className="opacity-0 px-3 py-1 rounded" style={{cursor: 'default'}}>
                        Wprowadź dodatkowe dane
                    </button>
                </div>
                <br/>
            </div>}
            {isLoading && <div className="text-2xl">Ładowanie...</div>}
            {!isLoading && numberOfPages === 0 && <div className="text-2xl">Brak próbek spełniających filtry</div>}
        </div>
    )
}

export default SampleList
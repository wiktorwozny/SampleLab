import { useEffect, useState } from "react"
import { Div } from "./ui/Div"
import { Sample, SortingAndPaginationRequest, SortingAndPaginationResponse } from "../utils/types"
import { Button } from "./ui/Button"
import { getAllSamples, getSortedAndPaginatedSamples, getNumberOfSamples } from "../helpers/samplingApi"
import { useNavigate } from "react-router-dom"
import {generateReportForSample} from "../helpers/generateReportApi";
import { MdKeyboardDoubleArrowLeft, MdKeyboardDoubleArrowRight, MdKeyboardArrowLeft, MdKeyboardArrowRight } from "react-icons/md";

const SampleList = () => {

    const [samples, setSamples] = useState<SortingAndPaginationResponse []>([])
    const [isLoading, setIsLoading] = useState<boolean>(true)
    const navigate = useNavigate()
    const [request, setRequest] = useState<SortingAndPaginationRequest>({
        fieldName: 'id',
        ascending: true,
        pageNumber: 0,
        pageSize: 10
    })
    const [activeColumn, setActiveColumn] = useState<string>('id');
    const [numberOfSamples, setNumberOfSamples] = useState<number>(0);
    const [numberOfPages, setNumberOfPages] = useState<number>(0);
    
    useEffect(()=>{
        const getCount = async() => {
            try {
                let response = await getNumberOfSamples()
                if(response.status === 200){
                    setNumberOfSamples(response.data)
                }
            } catch(err) {
                console.log(err);
            }
        }

        const getSamples = async() => {
            try {
                let response = await getSortedAndPaginatedSamples(request);
                if(response.status === 200){
                    setSamples(response.data)
                    setIsLoading(false)
                }
            } catch(err) {
                console.log(err);
            }
        }

        getCount();
        getSamples();
    },[request])

    useEffect(() => {
        setNumberOfPages(Math.ceil(numberOfSamples / request.pageSize))
    }, [numberOfSamples, request.pageSize])

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
        <div className="mt-10">
            {!isLoading && <div>
                <table className="table table-hover table-bordered">
                    <thead>
                        <tr>
                            <th scope="col" className={activeColumn === 'id' ? '!bg-gray-400' : '!bg-gray-300'} onClick={() => updateSortParams("id")}>ID</th>
                            <th scope="col" className={activeColumn === 'code' ? '!bg-gray-400' : '!bg-gray-300'} onClick={() => updateSortParams("code")}>Kod próbki</th>
                            <th scope="col" className={activeColumn === 'client' ? '!bg-gray-400' : '!bg-gray-300'} onClick={() => updateSortParams("client")}>Nazwa klienta</th>
                            <th scope="col" className={activeColumn === 'admissionDate' ? '!bg-gray-400' : '!bg-gray-300'} onClick={() => updateSortParams("admissionDate")}>Data przyjęcia</th>
                            <th scope="col" className={activeColumn === 'expirationDate' ? '!bg-gray-400' : '!bg-gray-300'} onClick={() => updateSortParams("expirationDate")}>Data przydatności</th>
                        </tr>
                    </thead>
                    <tbody>
                        {samples.map(sample => (
                        <tr key = {sample.id} onClick={()=>{navigate(`/sample/${sample.id}`)}}>
                            <td>{sample.id}</td>
                            <td>{sample.code}</td>
                            <td>{sample.clientName}</td>
                            <td>{sample.admissionDate.toString()}</td>
                            <td>{sample.expirationDate.toString()}</td>
                        </tr>))}
                    </tbody>
                </table>
                <div className="flex flex-row justify-center items-center">
                    <div className="mr-5">Pokaż: <input className="w-10 border" type="number" defaultValue={request.pageSize} onChange={(e) => {
                        setRequest(prevRequest => ({
                            ...prevRequest,
                            pageSize: Number(e.target.value) > 0 ? Number(e.target.value) : request.pageSize,
                            pageNumber: 0
                        }));
                    }}/></div>
                    <MdKeyboardDoubleArrowLeft className="bg-gray-200" onClick={() => updatePageNumber(0)}/>
                    <MdKeyboardArrowLeft className="bg-gray-200 mx-1" onClick={() => updatePageNumber(Math.max(0, request.pageNumber - 1))}/>
                    <div>Strona {request.pageNumber + 1} z {numberOfPages}</div>
                    <MdKeyboardArrowRight className="bg-gray-200 mx-1" onClick={() => updatePageNumber(Math.min(numberOfPages - 1, request.pageNumber + 1))}/>
                    <MdKeyboardDoubleArrowRight className="bg-gray-200" onClick={() => updatePageNumber(numberOfPages - 1)}/>
                </div>
                <br />
                <Button className="mt-2" type="button" onClick={()=>{navigate('/addSample')}}>Dodaj nową próbkę</Button>
            </div>}
            {isLoading && <div className="text-2xl">Loading...</div>}
        </div>
    )
}

export default SampleList
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getSampleById } from "../helpers/sampleApi";
import { Sample } from "../utils/types";
import { Div } from "../components/ui/Div";
import { Button } from "../components/ui/Button";
const SingleSamplePage = () => {
    let { sampleId } = useParams();
    const [sample, setSample] = useState<Sample>();
    const navigate = useNavigate()

    useEffect(()=>{
        const getSample = async() => {
            try{
                let response = await getSampleById(sampleId)
                if(response?.status === 200){
                    setSample(response.data)
                    console.log(response)
                }
            }catch(err){
                console.log(err)
            }
        }
        getSample()
    },[])
    return(<div className="flex flex-col justify-center items-center">
        <h2 className="text-2xl text-center font-bold my-3">Widok szczegółowy próbki</h2>
        <Div className="text-start">
            <span className="font-bold">Data przyjęcia: </span> 
            {`${sample?.admissionDate}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Do analizy: </span> 
            {sample?.analysis===true?"Tak":"Nie"}
        </Div>

        <Div className="flex justify-between">
            <div>
                <span className="font-bold">Nazwa Klient:</span> {`${sample?.client.name}`}
            </div>
            <div>
                <span className="font-bold">Adres Klienta:</span> {`${sample?.client.address.street}, ${sample?.client.address.city}`}
            </div>
        </Div>

        <Div className="text-start">
            <span className="font-bold">Asortyment: </span> 
            {`${sample?.assortment}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Data zakonczenia badan: </span> 
            {`${sample?.examinationEndDate}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Komentarz: </span> 
            {`${sample?.expirationComment}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Grupa: </span> 
            {`${sample?.group.name}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Inspekcja: </span> 
            {`${sample?.inspection?.name}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Norma: </span> 
            {`${sample?.samplingStandard?.name}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Wielkość: </span> 
            {`${sample?.size}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Status: </span> 
            {`${sample?.state}`}
        </Div>

        <div className="flex justify-between w-3/4">
            <Button type="button" onClick={()=>{navigate(`/sample/addReportData/${sampleId}`)}}>Dodaj dodatkowe informacje</Button>
            <Button type="button">Dodaj badanie</Button>
        </div>
    </div>)
}

export default SingleSamplePage
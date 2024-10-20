import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {getSampleById} from "../helpers/sampleApi";
import {Sample} from "../utils/types";
import {Div} from "../components/ui/Div";
import {DisableButton, StandardButton} from "../components/ui/StandardButton";
import {generateReportForSample} from "../helpers/generateReportApi";
import {ProgressStateEnum} from "../utils/enums";
import { checkResponse } from "../utils/checkResponse";

const SingleSamplePage = () => {
    let {sampleId} = useParams();
    const [sample, setSample] = useState<Sample>();

    const navigate = useNavigate()

    useEffect(() => {
        const getSample = async () => {
            try {
                let response = await getSampleById(sampleId)
                if (response?.status === 200) {
                    setSample(response.data)
                    console.log(response)
                }
            } catch (err) {
                console.log(err)
                checkResponse(err)
            }
        }
        getSample()
    }, [sampleId])

    const generateReport = async (sampleId: number) => {
        try {
            let response = await generateReportForSample(sampleId);
            console.log(response);

            if (response != null) {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const a = document.createElement('a');
                a.href = url;
                a.download = `report${sampleId}.docx`;
                document.body.appendChild(a);
                a.click();
                a.remove();
            }
        } catch (e) {
            console.log(e);
        }
    }

    return (<div className="flex flex-col justify-center items-center w-full">
        <h2 className="text-2xl text-center font-bold my-3">Widok szczegółowy próbki</h2>
        <Div className="text-start">
            <span className="font-bold">Data przyjęcia: </span>
            {`${sample?.admissionDate}`}
        </Div>

        <Div className="flex justify-between">
            <div>
                <span className="font-bold">Nazwa Klienta:</span> {`${sample?.client.name}`}
            </div>
            <div>
                <span
                    className="font-bold">Adres Klienta:</span> {`${sample?.client.address.street}, ${sample?.client.address.city}`}
            </div>
        </Div>

        <Div className="text-start">
            <span className="font-bold">Grupa: </span>
            {`${sample?.assortment.group.name}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Asortyment: </span>
            {`${sample?.assortment.name}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Norma pobrania próbki: </span>
            {`${sample?.samplingStandard?.name}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Data zakończenia badań: </span>
            {`${sample?.examinationExpectedEndDate}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Komentarz: </span>
            {`${sample?.expirationComment}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Kontrola: </span>
            {`${sample?.inspection?.name}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Wielkość próbki: </span>
            {`${sample?.size}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Stan próbki: </span>
            {`${sample?.state}`}
        </Div>

        <Div className="text-start">
            <span className="font-bold">Analiza odwoławcza: </span>
            {sample?.analysis === true ? "Tak" : "Nie"}
        </Div>

        <div className="flex justify-between w-3/4 p-3">
            <StandardButton type="button" onClick={() => {
                navigate(`/sample/addReportData/${sampleId}`)
            }}>Dodaj dodatkowe informacje</StandardButton>
            <StandardButton type="button" onClick={() => {
                navigate(`/sample/manageExaminations/${sampleId}`)
            }}>Zarządzaj badaniami</StandardButton>
            <StandardButton type="button" onClick={(e) => {
                e.stopPropagation();
                generateReport(Number(sampleId));
            }}>Generuj raport</StandardButton>
        </div>
    </div>)
}

export default SingleSamplePage
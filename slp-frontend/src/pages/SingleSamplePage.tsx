import {useContext, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {deleteSample, getSampleById} from "../helpers/sampleApi";
import {Sample} from "../utils/types";
import {Div} from "../components/ui/Div";
import {DisableButton, StandardButton} from "../components/ui/StandardButton";
import {generateReportForSample} from "../helpers/generateReportApi";
import {ProgressStateEnum} from "../utils/enums";
import ConfirmPopup from "../components/ui/ConfirmPopup";
import { AlertContext } from "../contexts/AlertsContext";
import { checkResponse } from "../utils/checkResponse";
import {Dropdown} from "react-bootstrap";

const SingleSamplePage = () => {
    let {sampleId} = useParams();
    const [sample, setSample] = useState<Sample>();
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const {setAlertDetails} = useContext(AlertContext);
    const [openReportDropdown, setOpenReportDropdown] = useState(false);

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

    const generateReport = async (sampleId: number, reportType: string) => {
        try {
            let response = await generateReportForSample(sampleId, reportType);
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

    const deleteSampleFunction = async () => {
        if (sampleId === undefined) {
            setAlertDetails({type: "error", isAlert: true, message: "Wystąpił błąd"})
            navigate('/')
            setIsPopupOpen(false)
            return
        }
        try {
            let response = await deleteSample(sampleId)
            console.log(response)
            if (response.status === 200) {
                setAlertDetails({type: "success", isAlert: true, message: "Udało ci się usunąć próbkę"})
                navigate('/')
            }
        } catch (err: any) {
            console.log(err)
            setAlertDetails({type: "error", isAlert: true, message: "Nie udało ci się usunąć próbki"})            
        }
        setIsPopupOpen(false)
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

        <div className="flex justify-center w-3/4 p-3 gap-2">
            <StandardButton type="button" onClick={() => {
                navigate(`/sample/addReportData/${sampleId}`)
            }}>Dodaj dodatkowe informacje</StandardButton>
            <StandardButton type="button" onClick={() => {
                navigate(`/sample/manageExaminations/${sampleId}`)
            }}>Zarządzaj badaniami</StandardButton>
            <StandardButton type="button" className="!bg-red-500 hover:!bg-red-600" onClick={() => {
                setIsPopupOpen(true);
            }}>Usuń próbkę</StandardButton>
            <Dropdown>
                <Dropdown.Toggle
                    disabled={sample?.progressStatus !== ProgressStateEnum.DONE}
                    variant="primary"
                    id="dropdown-basic"
                    className="p-2 rounded self-center text-white border-0"
                    style={{
                        backgroundColor: sample?.progressStatus !== ProgressStateEnum.DONE ? 'rgb(229, 231, 235)' : 'rgb(14, 165, 233)',  // Grey when disabled, blue otherwise
                        color: sample?.progressStatus !== ProgressStateEnum.DONE ? 'rgb(107, 114, 128)' : 'white',
                        cursor: sample?.progressStatus !== ProgressStateEnum.DONE ? 'not-allowed' : 'pointer',
                        pointerEvents: sample?.progressStatus !== ProgressStateEnum.DONE ? 'none' : 'auto'
                    }}
                >
                    Generuj raport
                </Dropdown.Toggle>

                <Dropdown.Menu>
                    <Dropdown.Item onClick={() => generateReport(Number(sampleId), "F4")}>Raport F-4</Dropdown.Item>
                    <Dropdown.Item onClick={() => generateReport(Number(sampleId), "F5")}>Raport F-5</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        </div>

        <ConfirmPopup
                onConfirm={deleteSampleFunction}
                show={isPopupOpen}
                handleClose={() => setIsPopupOpen(false)}
                message="Czy na pewno chcesz usunąć próbkę wraz z wykonanymi badaniami?"
            />
    </div>)
}

export default SingleSamplePage
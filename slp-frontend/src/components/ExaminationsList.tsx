import {FC, useEffect, useState} from "react";
import {Div} from "./ui/Div"
import {useNavigate, useParams} from "react-router-dom";
import {Examination, Indication} from "../utils/types";
import {getIndicationsForSample} from "../helpers/indicationApi";
import {deleteExamination, getExaminationsForSample} from "../helpers/examinationApi";
import {CancelButton, StandardButton} from "./ui/StandardButton";
import {generateKzwaForSample} from "../helpers/generateReportApi";
import {checkResponse} from "../utils/checkResponse";
import {useAppContext} from "../contexts/AppContext";

const ExaminationsList: FC<{}> = () => {

    const {sampleId} = useParams();
    const navigate = useNavigate()

    const [indications, setIndications] = useState<Indication[]>([]);
    const [examinations, setExaminations] = useState<Examination[]>([]);
    const [checkedStates, setCheckedStates] = useState<Record<number, Examination>>({});
    const [isLoading, setIsLoading] = useState<boolean>(true)
    const [hasOrganoleptic, setHasOrganoleptic] = useState<boolean>(false);

    useEffect(() => {
        const getIndicationsAndExaminations = async () => {
            if (!sampleId) return;

            try {
                const [indicationsResponse, examinationsResponse] = await Promise.all([
                    getIndicationsForSample(sampleId),
                    getExaminationsForSample(sampleId),
                ]);

                if (indicationsResponse?.status === 200) {
                    setIndications(indicationsResponse.data ?? []);
                    const hasOrganoleptic = indicationsResponse.data.some((indication: Indication) => indication.isOrganoleptic);
                    setHasOrganoleptic(hasOrganoleptic);
                }

                if (examinationsResponse?.status === 200) {
                    setExaminations(examinationsResponse.data ?? []);
                }

                initializeCheckedStates(indicationsResponse?.data ?? [], examinationsResponse?.data ?? []);
            } catch (err) {
                console.log(err);
                checkResponse(err);
            } finally {
                setIsLoading(false);
            }
        };

        getIndicationsAndExaminations();
    }, [sampleId, getIndicationsForSample, getExaminationsForSample]);

    const initializeCheckedStates = (indications: Indication[], examinations: Examination[]) => {
        const initialStates: Record<number, Examination> = {};
        indications.forEach(indication => {
            const examination = examinations.find(examination => examination.indication.id === indication.id);
            if (examination !== undefined) {
                initialStates[indication.id] = examination;
            }
        });

        setCheckedStates(initialStates);
    }

    const handleNavigation = (sampleId: any, examinationId: number | null, indicationId: number) => {
        if (examinationId === null) {
            navigate(`/sample/manageExaminations/${sampleId}/newExamination`, {state: {indicationId: indicationId}});
        } else {
            navigate(`/sample/manageExaminations/${sampleId}/newExamination/${examinationId}`, {
                state: {indicationId},
            });
        }
    }

    const {isLoadingOverlayVisible, toggleVisibility} = useAppContext();

    const generateKzwa = async (sampleId: number) => {
        if (isLoadingOverlayVisible) {
            return;
        }
        toggleVisibility();
        try {
            let response = await generateKzwaForSample(sampleId);
            console.log(response);

            if (response != null) {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const a = document.createElement('a');
                a.href = url;
                a.download = `kzwa${sampleId}.xlsx`;
                document.body.appendChild(a);
                a.click();
                a.remove();
            }
        } catch (e) {
            console.log(e);
        } finally {
            toggleVisibility();
        }
    }

    const handleCheckboxChange = async (indicationId: number, examinationId: number) => {
        setCheckedStates(prevState => {
            const newState = {...prevState};
            if (newState[indicationId]) {
                const confirmed = window.confirm("Czy na pewno? Badanie zostanie usunięte!");
                if (!confirmed) {
                    return prevState;
                }

                try {
                    let response = deleteExamination(examinationId);
                    console.log(response);
                } catch (err) {
                    console.log(err);
                }

                delete newState[indicationId];
            } else {
                handleNavigation(sampleId, null, indicationId);
            }
            return newState;
        });
    };

    return (
        <div className="indications-list flex flex-col items-center h-fit justify-center w-full">
            <div>
                <h1 className="text-center font-bold text-3xl w-full my-2">Oznaczenia</h1>
                <h1 className="text-center font-bold text-2xl w-full my-2">dla próbki nr: {sampleId}</h1>
            </div>
            {!isLoading && indications.map(indication => (
                (!indication.isOrganoleptic && (
                    <Div key={indication.id} className="flex justify-between hover:bg-slate-100 cursor-default">
                        <div className="flex items-center">
                            <input
                                id="link-checkbox"
                                type="checkbox"
                                checked={checkedStates[indication.id] !== undefined}
                                onChange={() => handleCheckboxChange(indication.id, checkedStates[indication.id]?.id)}
                                className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"></input>
                            <span className="font-bold ml-2">{indication.name}</span>
                            <span className="ml-2">{indication.method}</span>
                        </div>
                        {checkedStates[indication.id] && (
                            <StandardButton type="button" onClick={() => {
                                const examination = checkedStates[indication.id];
                                handleNavigation(sampleId, examination.id, indication.id);
                            }}>Edytuj</StandardButton>
                        )}
                    </Div>))
            ))}
            {hasOrganoleptic && <h1 className="font-bold text-xl w-full my-2">Cechy organoleptyczne</h1>}
            {!isLoading && indications.map(indication => (
                (indication.isOrganoleptic && (
                    <Div key={indication.id} className="flex justify-between hover:bg-slate-100 cursor-default">
                        <div className="flex items-center">
                            <input
                                id="link-checkbox"
                                type="checkbox"
                                checked={checkedStates[indication.id] !== undefined}
                                onChange={() => handleCheckboxChange(indication.id, checkedStates[indication.id]?.id)}
                                className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"></input>
                            <span className="font-bold ml-2">{indication.name}</span>
                        </div>
                        {checkedStates[indication.id] && (
                            <StandardButton type="button" onClick={() => {
                                const examination = checkedStates[indication.id];
                                handleNavigation(sampleId, examination.id, indication.id);
                            }}>Edytuj</StandardButton>
                        )}
                    </Div>))
            ))}
            <StandardButton type='button' className='mt-3' onClick={
                (e) => {
                    e.stopPropagation();
                    generateKzwa(Number(sampleId));
                }
            }>Generuj KZWA</StandardButton>
            <CancelButton type='button' className='mt-3'
                          onClick={() => navigate(`/sample/${sampleId}`)}>Powrót</CancelButton>
        </div>
    );
}

export default ExaminationsList;

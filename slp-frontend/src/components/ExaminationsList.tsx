import {FC, useEffect, useState} from "react";
import { Div } from "./ui/Div"
import {useParams} from "react-router-dom";
import {Indication, Examination} from "../utils/types";
import {getIndicationsForSample} from "../helpers/indicationApi";
import { useNavigate } from "react-router-dom"
import {deleteExamination, getExaminationsForSample} from "../helpers/examinationApi";
import {Button} from "./ui/Button";

const ExaminationsList: FC<{}> = () => {

    const {sampleId} = useParams();
    const navigate = useNavigate()

    const [indications, setIndications] = useState<Indication[]>([]);
    const [examinations, setExaminations] = useState<Examination[]>([]);
    const [checkedStates, setCheckedStates] = useState<Record<number, Examination>>({});
    const [isLoading, setIsLoading] = useState<boolean>(true)

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
                }

                if (examinationsResponse?.status === 200) {
                    setExaminations(examinationsResponse.data ?? []);
                }

                initializeCheckedStates(indicationsResponse?.data ?? [], examinationsResponse?.data ?? []);
            } catch (err) {
                console.log(err);
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
            navigate(`/sample/manageExaminations/${sampleId}/newExamination`, { state: { indicationId: indicationId } });
        } else {
            navigate(`/sample/manageExaminations/${sampleId}/newExamination/${examinationId}`, {
                state: { indicationId },
            });
        }
    }

    const handleCheckboxChange = async (indicationId: number, examinationId: number) => {
        setCheckedStates(prevState => {
            const newState = { ...prevState };
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
        <div className="indications-list flex flex-col items-center h-fit justify-center">
            <div>
                <h1 className="text-center font-bold text-3xl w-full my-2">Oznaczenia</h1>
                <h1 className="text-center font-bold text-2xl w-full my-2">dla próbki nr: {sampleId}</h1>
            </div>
            {!isLoading && indications.map(indication => (
                <Div key={indication.id} className="flex justify-between hover:bg-slate-100 cursor-default">
                    <div>
                        <input
                            id="link-checkbox"
                            type="checkbox"
                            checked={checkedStates[indication.id] !== undefined}
                            onChange={() => handleCheckboxChange(indication.id, checkedStates[indication.id]?.id)}
                            className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"></input>
                        <span className="font-bold ml-2">{indication.name}</span>
                    </div>
                    {checkedStates[indication.id] && (
                        <Button type="button" onClick={() => {
                            const examination = checkedStates[indication.id];
                            handleNavigation(sampleId, examination.id, indication.id);
                        }}>Wprowadź wyniki badań</Button>
                    )}
                </Div>
            ))}
        </div>
    );
}

export default ExaminationsList;

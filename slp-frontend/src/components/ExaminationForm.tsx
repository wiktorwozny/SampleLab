import {FC, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {getExaminationById} from "../helpers/examinationApi";

const ExaminationForm: FC<{}> = () => {

    const {sampleId, examinationId} = useParams();
    const [examination, setExamination] = useState(false);

    useEffect(() => {
        if (examinationId) {
            const getExamination = async () => {
                try {
                    let response = await getExaminationById(examinationId);
                    if (response?.status === 200) {
                        setExamination(response.data);
                        console.log(examination);
                    }
                } catch (err) {
                    console.log(err);
                }
            }

            getExamination();
        } else {
            console.log("ni ma");
        }
    }, [examinationId, getExaminationById]);

    return <div>cos</div>
}

export default ExaminationForm;

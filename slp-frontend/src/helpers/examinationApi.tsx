import axios from "axios";
import { backendUrl } from "../utils/urls";

const url = 'examination/'

const getExaminationsForSample = (sampleId: string | undefined) => {
    if (sampleId !== undefined) {
        return axios.get(backendUrl + url + `sample/${sampleId}`)
    }
    return null;
}

const getExaminationById = (examinationId: string | undefined) => {
    if (examinationId !== undefined) {
        return axios.get(backendUrl + url + `${examinationId}`);
    }
    return null;
}

export {
    getExaminationsForSample,
    getExaminationById
}

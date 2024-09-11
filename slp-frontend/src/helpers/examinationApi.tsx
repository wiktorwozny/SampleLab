import axios from "axios";
import {backendUrl} from "../utils/urls";
import {Examination} from "../utils/types";

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

const updateExamination = (updatedExamination: Examination) => {
    return axios.put(backendUrl + url + 'update', updatedExamination);
}

const addExamination = (examination: Examination) => {
    return axios.post(backendUrl + url + "save", examination);
}

const deleteExamination = (examinationId: number) => {
    return axios.delete(backendUrl + url + `delete/${examinationId}`);
}

export {
    getExaminationsForSample,
    getExaminationById,
    updateExamination,
    addExamination,
    deleteExamination,
}

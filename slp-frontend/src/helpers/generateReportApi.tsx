import axios from "axios";
import { backendUrl, Header } from "../utils/urls";

const url = 'generate-report/'

const generateReportForSample = (sampleId: number | undefined) => {
    if (sampleId !== undefined) {
        return axios.get(backendUrl + url + `sample-report/${sampleId}`, {
            responseType: 'blob',
            ...Header()
        });
    }
    return null;
}

const generateKzwaForSample = (sampleId: number | undefined) => {
    if (sampleId !== undefined) {
        return axios.get(backendUrl + url + `kzwa-report/${sampleId}`, {
            responseType: 'blob',
            ...Header()
        });
    }
    return null;
}

export {
    generateReportForSample,
    generateKzwaForSample,
}

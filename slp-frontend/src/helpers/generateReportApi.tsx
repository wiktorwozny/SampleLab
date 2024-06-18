import axios from "axios";
import { backendUrl } from "../utils/urls";

const url = 'generate-report/'

const generateReportForSample = (sampleId: number | undefined) => {
    if (sampleId !== undefined) {
        return axios.post(backendUrl + url + `sample-report/${sampleId}`);
    }
    return null;
}

const generateKzwaForSample = (sampleId: number | undefined) => {
    if (sampleId !== undefined) {
        return axios.post(backendUrl + url + `kzwa-report/${sampleId}`);
    }
    return null;
}

export {
    generateReportForSample,
    generateKzwaForSample,
}

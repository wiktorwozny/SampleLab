import axios from "axios";
import { backendUrl } from "../utils/urls";

const url = 'generate-report/'

const generateReportForSample = (sampleId: number | undefined) => {
    if (sampleId !== undefined) {
        return axios.post(backendUrl + url + `generate/${sampleId}`);
    }
    return null;
}

export {
    generateReportForSample,
}

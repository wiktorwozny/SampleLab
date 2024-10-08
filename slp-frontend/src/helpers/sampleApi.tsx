import axios from "axios"
import {backendUrl, Header} from "../utils/urls"
import {ReportData} from "../utils/types"

const url = `sample/`

const getAllSamples = () => {
    return axios.get(backendUrl + url + `list`, Header())
}

const addReportDataToSample = (sampleId: string | undefined, reportData: ReportData) => {
    if (sampleId !== undefined) {
        return axios.post(backendUrl + url + `${sampleId}/report-data`, reportData, Header())
    }
    return null;
}

const updateStatus = (sampleId: number, status: string) => {
    console.log(String(status));
    return axios.put(backendUrl + url + `status/${sampleId}/${status}`, {}, Header())
}

const getSampleById = (sampleId: string | undefined) => {
    if (sampleId !== undefined) {
        return axios.get(backendUrl + url + sampleId, Header())
    }
    return null;
}
export {
    updateStatus,
    getAllSamples,
    addReportDataToSample,
    getSampleById
}
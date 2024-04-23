import axios from "axios"
import { backendUrl } from "../utils/urls"
import { ReportData } from "../utils/types"
const url = `sample/`

const getAllSamples = () => {
    return axios.get(backendUrl + url + `list`)
}

const addReportDataToSample = (sampleId:string | undefined, reportData:ReportData) => {
    if(sampleId !== undefined){
        return axios.post(backendUrl + url + `${sampleId}/report-data`, reportData)
    }
    return null;
}

const getSampleById = (sampleId:string | undefined) => {
    if(sampleId !== undefined){
        return axios.get(backendUrl + url + `get-sample/${sampleId}`)
    }
    return null;
}
export {
    getAllSamples,
    addReportDataToSample,
    getSampleById
}
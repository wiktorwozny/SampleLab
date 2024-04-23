import axios from "axios"
import { backendUrl } from "../utils/urls"
import { ReportData } from "../utils/types"
const url = `sample/`

const getAllSamples = () => {
    return axios.get(backendUrl + url + `list`)
}

const addReportDataToSample = (sampleId:number, reportData:ReportData) => {
    return axios.post(backendUrl + url + `${sampleId}/report-data`, reportData)
}

export {
    getAllSamples,
    addReportDataToSample
}
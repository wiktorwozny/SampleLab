import axios from "axios"
import { backendUrl } from "../utils/urls"
import { ReportData } from "../utils/types"

const url = "report-data/"

const getAllReportData = () => {
    return axios.get(backendUrl + url + "list")
}

const addReportDataToSample = (sampleId:number, reportData:ReportData) => {
    return axios.post(backendUrl + url + `${sampleId}/save`, reportData)
}

export {
    getAllReportData,
    addReportDataToSample
}
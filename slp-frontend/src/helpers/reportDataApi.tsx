import axios from "axios"
import { backendUrl } from "../utils/urls"
import { ReportData } from "../utils/types"

const url = "report-data/"

const getAllReportData = () => {
    return axios.get(backendUrl + url + "list")
}

const addReportData = (reportData:ReportData) => {
    return axios.post(backendUrl + url + "save", reportData)
}

const getReportDataBySampleId = (sampleId:string | undefined) => {
    return axios.get(backendUrl + url + `sample/${sampleId}`)
}

export {
    getAllReportData,
    addReportData,
    getReportDataBySampleId
}
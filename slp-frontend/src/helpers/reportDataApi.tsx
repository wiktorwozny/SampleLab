import axios from "axios"
import { backendUrl } from "../utils/urls"

const url = "report-data/"

const getAllReportData = () => {
    return axios.get(backendUrl + url +"list")
}

export {
    getAllReportData
}
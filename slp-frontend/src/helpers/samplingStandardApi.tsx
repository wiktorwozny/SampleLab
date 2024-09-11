import axios from "axios"
import { backendUrl } from "../utils/urls"

const url = "sampling-standard/"

const getAllSamplingStandard = () => {
    return axios.get(backendUrl + url + "list")
}

export {
    getAllSamplingStandard
}
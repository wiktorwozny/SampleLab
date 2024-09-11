import axios from "axios"
import { backendUrl, Header } from "../utils/urls"

const url = "sampling-standard/"

const getAllSamplingStandard = () => {
    return axios.get(backendUrl + url + "list", Header())
}

export {
    getAllSamplingStandard
}
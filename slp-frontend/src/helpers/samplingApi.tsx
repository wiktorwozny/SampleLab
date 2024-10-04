import axios from "axios"
import { backendUrl, Header } from "../utils/urls"
import { Sample, FilterRequest } from "../utils/types"
const url = "sample/"

const getAllSamples = () => {
    return axios.get(backendUrl + url + "list", Header())
}

const getFilteredSamples = (request: FilterRequest) => {
    return axios.put(backendUrl + url + "list/filtered", request, Header())
}

const getNumberOfSamples = () => {
    return axios.get(backendUrl + url + "count", Header())
}

const addSample = (sample: Sample) => {
    return axios.post(backendUrl + url + "save", sample, Header())
}

export{
    getAllSamples,
    getFilteredSamples,
    addSample,
    getNumberOfSamples
}
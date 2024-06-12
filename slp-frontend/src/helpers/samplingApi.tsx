import axios from "axios"
import { backendUrl } from "../utils/urls"
import { Sample, FilterRequest } from "../utils/types"
const url = "sample/"

const getAllSamples = () => {
    return axios.get(backendUrl + url + "list")
}

const getFilteredSamples = (request: FilterRequest) => {
    return axios.put(backendUrl + url + "list/filtered", request)
}

const getNumberOfSamples = () => {
    return axios.get(backendUrl + url + "count")
}

const addSample = (sample: Sample) => {
    return axios.post(backendUrl + url + "save", sample)
}

export{
    getAllSamples,
    getFilteredSamples,
    addSample,
    getNumberOfSamples
}
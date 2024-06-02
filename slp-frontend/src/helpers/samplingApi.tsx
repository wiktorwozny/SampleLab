import axios from "axios"
import { backendUrl } from "../utils/urls"
import { Sample, SortingAndPaginationRequest } from "../utils/types"
const url = "sample/"

const getAllSamples = () => {
    return axios.get(backendUrl + url + "list")
}

const getSortedAndPaginatedSamples = (request: SortingAndPaginationRequest) => {
    return axios.put(backendUrl + url + "list/sorted-and-paginated", request)
}

const getNumberOfSamples = () => {
    return axios.get(backendUrl + url + "count")
}

const addSample = (sample: Sample) => {
    return axios.post(backendUrl + url + "save", sample)
}

export{
    getAllSamples,
    getSortedAndPaginatedSamples,
    addSample,
    getNumberOfSamples
}
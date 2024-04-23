import axios from "axios"
import { backendUrl } from "../utils/urls"
import { Sample } from "../utils/types"
const url = "sample/"

const getAllSamples = () => {
    return axios.get(backendUrl + url + "list")
}

const addSample = (sample: Sample) => {
    return axios.post(backendUrl + url + "save", sample)
}

export{
    getAllSamples,
    addSample
}
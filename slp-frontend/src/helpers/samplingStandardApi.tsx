import axios from "axios"
import {backendUrl} from "../utils/urls"
import {SamplingStandards} from "../utils/types";

const url = "sampling-standard/"

const getAllSamplingStandard = () => {
    return axios.get(backendUrl + url + "list")
}
const updateSamplingStandard = (item: SamplingStandards) => {
    return axios.put(backendUrl + url + 'update', item);
}

const addSamplingStandard = (item: SamplingStandards) => {
    return axios.post(backendUrl + url + "save", item);
}

const deleteSamplingStandard = (id: number | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`);
}

export {
    getAllSamplingStandard,
    updateSamplingStandard,
    addSamplingStandard,
    deleteSamplingStandard
}
import axios from "axios"
import {SamplingStandards} from "../utils/types";
import { backendUrl, Header } from "../utils/urls"

const url = "sampling-standard/"

const getAllSamplingStandard = () => {
    return axios.get(backendUrl + url + "list", Header())
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
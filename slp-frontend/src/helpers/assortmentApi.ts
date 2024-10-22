import axios from "axios"
import {backendUrl, Header} from "../utils/urls"
import {AssortmentSave} from "../utils/types";

const url = "assortment/"

const getAllAssortments = () => {
    return axios.get(backendUrl + url + "list", Header())
}
const updateAssortment = (item: AssortmentSave) => {
    return axios.put(backendUrl + url + 'update', item, Header());
}

const addAssortment = (item: AssortmentSave) => {
    return axios.post(backendUrl + url + 'save', item, Header());
}

const deleteAssortment = (id: number | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`, Header());
}

export {
    getAllAssortments,
    updateAssortment,
    addAssortment,
    deleteAssortment
}

